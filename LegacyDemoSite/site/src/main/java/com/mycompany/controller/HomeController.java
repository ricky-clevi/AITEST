/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycompany.controller;

import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller for the home page
 */
@Controller
public class HomeController {

    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    /**
     * Handles the root URL and displays the home page
     */
    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {
        // Load products for the home page
        // Try to get the first category and display its products
        List<Category> categories = catalogService.findAllCategories();
        List<Product> products = null;
        
        if (categories != null && !categories.isEmpty()) {
            Category firstCategory = categories.get(0);
            products = catalogService.findActiveProductsByCategory(firstCategory, 10, 0);
        }
        
        model.addAttribute("products", products);
        
        return "layout/home";
    }
}