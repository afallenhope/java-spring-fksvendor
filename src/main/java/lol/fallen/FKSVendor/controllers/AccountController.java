/*
 * Copyright (c) 2024 Pierre C
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package lol.fallen.FKSVendor.controllers;

import jakarta.validation.Valid;
import lol.fallen.FKSVendor.dtos.LoginDto;
import lol.fallen.FKSVendor.dtos.RegisterDto;
import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.repositories.UserRoleRepository;
import lol.fallen.FKSVendor.repositories.VendorUserRepository;
import lol.fallen.FKSVendor.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private VendorUserRepository vendorUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth) {
        var response = new HashMap<String, Object>();

        try {

            response.put("authorities", auth.getAuthorities());

            var foundUser = vendorUserRepository.findByUsername(auth.getName());
            response.put("user", foundUser);

            if (null == foundUser) {
                response.put("error", "could not find user");
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            System.out.println("Exception");
            ex.printStackTrace();
            response.put("error", "Invalid Token");
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDto registerDto, BindingResult result) {
        if (result.hasErrors()) {
            var errorList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errorList.size(); i++) {
                var error = (FieldError) errorList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorsMap);
        }

        var bCryptEncoder = new BCryptPasswordEncoder();


        VendorUser vendorUser = new VendorUser();
        vendorUser.setFirstName(registerDto.getFirstName());
        vendorUser.setLastName(registerDto.getLastName());
        vendorUser.setUuid(registerDto.getUuid());
        vendorUser.setUsername(registerDto.getUsername());
        vendorUser.setCreatedAt(new Date());
        vendorUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));

        UserRole userRole = userRoleRepository.findByName("customer");

        if (null != userRole) {
            vendorUser.setRoles(Set.of(userRole));
        }

        var response = new HashMap<String, Object>();
        try {
            var foundUser = vendorUserRepository.findByUsername(registerDto.getUsername());

            if (null != foundUser) {
                response.put("error", "user exists");
                return ResponseEntity.badRequest().body(response);
            }

            foundUser = vendorUserRepository.findByUuid(registerDto.getUuid());
            if (null != foundUser) {
                response.put("error", "user exists");
                return ResponseEntity.badRequest().body(response);
            }

            vendorUserRepository.save(vendorUser);

            String jwtToken = jwtService.GenerateToken(vendorUser.getUsername()); //createJwtToken(vendorUser);
            response.put("token", jwtToken);
            response.put("user", vendorUser);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            System.out.println("Exception");
            ex.printStackTrace();
        }

        response.put("error", "could not register at this time.");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @Valid @RequestBody LoginDto loginDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            var errorList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errorList.size(); i++) {
                var error = (FieldError) errorList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        var response = new HashMap<String, Object>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            VendorUser vendorUser = vendorUserRepository.findByUsername(loginDto.getUsername());
            String jwtToken = jwtService.GenerateToken(vendorUser.getUsername()); //createJwtToken(vendorUser);
            response.put("token", jwtToken);
            response.put("user", vendorUser);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            System.out.println("Exception");
            ex.printStackTrace();
        }

        response.put("error", "incorrect username or password");
        return ResponseEntity.badRequest().body(response);
    }

//    private String createJwtToken(VendorUser vendorUser) {
//        Instant now = Instant.now();
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer(jwtIssuer)
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(24 * 3600))
//                .subject(vendorUser.getUsername())
//                .claim("role", vendorUser.getRole())
//                .build();
//
//        var encoder = new NimbusJwtEncoder(
//                new ImmutableSecret<>(jwtSecretKey.getBytes())
//        );
//
//        var params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
//
//        return encoder.encode(params).getTokenValue();
//    }
//
//    private boolean verifyJwtToken(String token) throws ParseException {
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(token);
//            JWSVerifier verifier = new MACVerifier(jwtSecretKey.getBytes());
//
//            if (!signedJWT.verify(verifier)) {
//                System.out.println("invalid signature");
//                return false;
//            }
//
//            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
//
//            Date expirationTime = claims.getExpirationTime();
//            if (null != expirationTime && new Date().after(expirationTime)) {
//                System.out.println("token expired");
//                return false;
//            }
//
//            return true;
//        } catch (Exception ex) {
//            System.out.println("Exception");
//            ex.printStackTrace();
//        }
//        return false;
//    }
}
