/*
 * Copyright (c) 2024
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

package lol.fallen.FKSVendor.commands;

import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.repositories.UserRoleRepository;
import lol.fallen.FKSVendor.repositories.VendorUserRepository;
import lol.fallen.FKSVendor.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Component
public class DatabaseSeederRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederRunner.class);

    @Autowired
    private VendorUserRepository vendorUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        userRoleRepository.save(new UserRole(null, "ROLE_USER"));
        userRoleRepository.save(new UserRole(null, "ROLE_MANAGER"));
        userRoleRepository.save(new UserRole(null, "ROLE_BLOGGER"));
        userRoleRepository.save(new UserRole(null, "ROLE_OWNER"));
        userRoleRepository.save(new UserRole(null, "ROLE_TRUSTED"));
        logger.info("Created {} roles", userRoleRepository.count());

        var bCryptEncoder = new BCryptPasswordEncoder();

        vendorUserRepository.save(new VendorUser(
                null,
                UUID.fromString("e6e73511-8a2c-4a11-acf6-6adeba0b99f9"),
                "fallen",
                "shadow",
                "fallen.shadow",

                bCryptEncoder.encode("supersecret"),
                new HashSet<UserRole>(),
                new Date())
        );

        vendorUserRepository.save(
                new VendorUser(
                        null,
                        UUID.fromString("e2dca752-503a-4629-83d2-d40278e20723"),
                        "sullen",
                        "nebula",
                        "sullen.nebula",
                        bCryptEncoder.encode("password"),
                        new HashSet<UserRole>(),
                        new Date()
                )
        );

        vendorUserRepository.save(
                new VendorUser(
                        null,
                        UUID.randomUUID(),
                        "test",
                        "user",
                        "test.user",
                        bCryptEncoder.encode("password"),
                        new HashSet<UserRole>(),
                        new Date()
                )
        );
        logger.info("Created {} users", vendorUserRepository.count());

        userService.addRoleToUser("fallen.shadow", "ROLE_OWNER");
        userService.addRoleToUser("fallen.shadow", "ROLE_MANAGER");

        userService.addRoleToUser("sullen.nebula", "ROLE_TRUSTED");
        userService.addRoleToUser("sullen.nebula", "ROLE_MANAGER");

        userService.addRoleToUser("test.user", "ROLE_USER");

        logger.info("Added roles to users");
    }

}
