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

package lol.fallen.FKSVendor.impl;

import lol.fallen.FKSVendor.models.VendorStore;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.services.VendorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VendorStoreServiceImpl implements VendorStoreService {

    /**
     * @param store
     * @return
     */
    @Override
    public VendorUser saveVendorStore(VendorStore store) {
        return null;
    }

    /**
     * @param user
     * @param store
     */
    @Override
    public void addOwner(VendorUser user, VendorStore store) {

    }

    /**
     * @param user
     * @param store
     */
    @Override
    public void addManager(VendorUser user, VendorStore store) {

    }

    /**
     * @param user
     * @param store
     */
    @Override
    public void addBlogger(VendorUser user, VendorStore store) {

    }

    /**
     * @param user
     * @param store
     */
    @Override
    public void addVip(VendorUser user, VendorStore store) {

    }

    /**
     * @param username
     * @param role
     */
    @Override
    public void addRoleToUser(String username, String role) {

    }

    /**
     * @param name
     * @return
     */
    @Override
    public VendorStore loadStoreByName(String name) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<VendorStore> getAllStores() {
        return List.of();
    }
}
