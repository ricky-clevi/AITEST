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

package com.mycompany.controller.cart;

import com.mycompany.core.cart2.domain.OrderCart2;
import com.mycompany.core.cart2.service.OrderCart2Service;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.inventory.service.InventoryUnavailableException;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.exception.AddToCartException;
import org.broadleafcommerce.core.order.service.exception.ProductOptionValidationException;
import org.broadleafcommerce.core.order.service.exception.RemoveFromCartException;
import org.broadleafcommerce.core.order.service.exception.RequiredAttributeNotProvidedException;
import org.broadleafcommerce.core.order.service.exception.UpdateCartException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.controller.cart.BroadleafCartController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.core.web.order.model.AddToCartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cart")
public class CartController extends BroadleafCartController {
    
    @Autowired
    private OrderCart2Service orderCart2Service;
    
    @Override
    @RequestMapping("")
    public String cart(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        return super.cart(request, response, model);
    }
    
    /*
     * The Heat Clinic does not show the cart when a product is added. Instead, when the product is added via an AJAX
     * POST that requests JSON, we only need to return a few attributes to update the state of the page. The most
     * efficient way to do this is to call the regular add controller method, but instead return a map that contains
     * the necessary attributes. By using the @ResposeBody tag, Spring will automatically use Jackson to convert the
     * returned object into JSON for easy processing via JavaScript.
     */
    @RequestMapping(value = "/add", produces = "application/json")
    public @ResponseBody Map<String, Object> addJson(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, AddToCartException {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
            super.add(request, response, model, addToCartItem);
            
            if (addToCartItem.getItemAttributes() == null || addToCartItem.getItemAttributes().size() == 0) {
                responseMap.put("productId", addToCartItem.getProductId());
            }
            responseMap.put("productName", catalogService.findProductById(addToCartItem.getProductId()).getName());
            responseMap.put("quantityAdded", addToCartItem.getQuantity());
            responseMap.put("cartItemCount", String.valueOf(CartState.getCart().getItemCount()));
            if (addToCartItem.getItemAttributes() == null || addToCartItem.getItemAttributes().size() == 0) {
                // We don't want to return a productId to hide actions for when it is a product that has multiple
                // product options. The user may want the product in another version of the options as well.
                responseMap.put("productId", addToCartItem.getProductId());
            }
        } catch (AddToCartException e) {
            if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
                responseMap.put("error", "allOptionsRequired");
            } else if (e.getCause() instanceof ProductOptionValidationException) {
                ProductOptionValidationException exception = (ProductOptionValidationException) e.getCause();
                responseMap.put("error", "productOptionValidationError");
                responseMap.put("errorCode", exception.getErrorCode());
                responseMap.put("errorMessage", exception.getMessage());
                //blMessages.getMessage(exception.get, lfocale))
            } else if (e.getCause() instanceof InventoryUnavailableException) {
                responseMap.put("error", "inventoryUnavailable");
            } else {
                throw e;
            }
        }
        
        return responseMap;
    }
    
    /*
     * The Heat Clinic does not support adding products with required product options from a category browse page
     * when JavaScript is disabled. When this occurs, we will redirect the user to the full product details page 
     * for the given product so that the required options may be chosen.
     */
    @RequestMapping(value = "/add", produces = { "text/html", "*/*" })
    public String add(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes,
            @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, AddToCartException {
        try {
            return super.add(request, response, model, addToCartItem);
        } catch (AddToCartException e) {
            Product product = catalogService.findProductById(addToCartItem.getProductId());
            return "redirect:" + product.getUrl();
        }
    }
    
    @RequestMapping("/updateQuantity")
    public String updateQuantity(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes,
            @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, UpdateCartException, RemoveFromCartException {
        try {
            return super.updateQuantity(request, response, model, addToCartItem);
        } catch (UpdateCartException e) {
            if (e.getCause() instanceof InventoryUnavailableException) {
                // Since there was an exception, the order gets detached from the Hibernate session. This re-attaches it
                CartState.setCart(orderService.findOrderById(CartState.getCart().getId()));
                if (isAjaxRequest(request)) {
                    model.addAttribute("errorMessage", "Not enough inventory to fulfill your requested amount of " + addToCartItem.getQuantity());
                    return getCartView();
                } else {
                    redirectAttributes.addAttribute("errorMessage", "Not enough inventory to fulfill your requested amount of " + addToCartItem.getQuantity());
                    return getCartPageRedirect();
                }
            } else {
                throw e;
            }
        }
    }
    
    @Override
    @RequestMapping("/remove")
    public String remove(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, RemoveFromCartException {
        return super.remove(request, response, model, addToCartItem);
    }
    
    @Override
    @RequestMapping("/empty")
    public String empty(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        //return super.empty(request, response, model);
        return "ajaxredirect:/";
        
    }
    
    @Override
    @RequestMapping("/promo")
    public String addPromo(HttpServletRequest request, HttpServletResponse response, Model model,
            @RequestParam("promoCode") String customerOffer) throws IOException, PricingException {
        return super.addPromo(request, response, model, customerOffer);
    }
    
    @Override
    @RequestMapping("/promo/remove")
    public String removePromo(HttpServletRequest request, HttpServletResponse response, Model model,
            @RequestParam("offerCodeId") Long offerCodeId) throws IOException, PricingException {
        return super.removePromo(request, response, model, offerCodeId);
    }
    
    /**
     * Cart2에 상품 추가 (Buy More 버튼용)
     * 한 번에 5개씩 추가
     */
    @RequestMapping(value = "/add2", produces = "application/json")
    public @ResponseBody Map<String, Object> addToCart2(HttpServletRequest request, HttpServletResponse response, Model model,
            @RequestParam("productId") Long productId,
            @RequestParam("skuId") Long skuId,
            @RequestParam(value = "quantity", defaultValue = "5") int quantity) throws IOException, PricingException {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        
        try {
            // 현재 사용자 ID 가져오기 (임시로 1 사용)
            Long customerId = 1L;
            
            // Cart2에 상품 추가
            orderCart2Service.addItemToCart2(customerId, productId, skuId, quantity);
            
            // Cart2 정보 가져오기
            OrderCart2 cart2 = orderCart2Service.getOrCreateCart2(customerId);
            
            Product product = catalogService.findProductById(productId);
            
            responseMap.put("productId", productId);
            responseMap.put("productName", product != null ? product.getName() : "Unknown Product");
            responseMap.put("quantityAdded", quantity);
            responseMap.put("cart2ItemCount", String.valueOf(cart2.getItemCount()));
            responseMap.put("cart2Id", cart2.getId());
            responseMap.put("success", true);
            
        } catch (Exception e) {
            responseMap.put("error", "Error adding to cart2: " + e.getMessage());
            responseMap.put("success", false);
        }
        
        return responseMap;
    }
    
    /**
     * Cart2 정보 조회
     */
    @RequestMapping(value = "/cart2", produces = "application/json")
    public @ResponseBody Map<String, Object> getCart2(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        
        try {
            Long customerId = 1L;
            OrderCart2 cart2 = orderCart2Service.getOrCreateCart2(customerId);
            
            responseMap.put("cart2Id", cart2.getId());
            responseMap.put("cart2ItemCount", cart2.getItemCount());
            responseMap.put("cart2Total", cart2.getTotal());
            responseMap.put("success", true);
            
        } catch (Exception e) {
            responseMap.put("error", "Error getting cart2: " + e.getMessage());
            responseMap.put("success", false);
        }
        
        return responseMap;
    }
    
}
