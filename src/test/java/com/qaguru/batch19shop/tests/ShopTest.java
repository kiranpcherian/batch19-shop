package com.qaguru.batch19shop.tests;
import com.qaguru.batch19shop.models.Product;
import com.qaguru.batch19shop.services.ProductService;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.List;


public class ShopTest {


    @Test
    public void sampleTest(){
        System.out.println("My test");

 //       String sNo = "1800.26";
//        double dNo = Double.valueOf(sNo);

    }
    @Test
    public void saveANewProduct() {
        String file = "testdata/product.json";

        ProductService productService = new ProductService();
        Product product = productService.readProductDetails(file);
        String productId = productService.saveANewProduct(product);
        productService.findAProduct(productId,product,HttpStatus.SC_OK);

    }
    @Test
    public void updateProduct(){
        String file = "testdata/product.json";
        ProductService productService = new ProductService();
        Product product = productService.readProductDetails(file);
        String productId = productService.saveANewProduct(product);
//        Product product2 =new Product();
//        product2.setName("Samsung S20");
//        product2.setDescription("A fully featured phone");
//        product2.setPrice(1600.00);

        Product product2 = Product.builder()
                .name("Samsung 20")
                .description("A featured phone")
                .price(1700.99)
                .build();
        productService.updateAProduct(productId,product2);
        productService.findAProduct(productId,product2,HttpStatus.SC_OK);

    }
    @Test
    public void deleteProduct(){
        String file = "testdata/product.json";
        ProductService productService = new ProductService();
        Product product = productService.readProductDetails(file);
        String productId = productService.saveANewProduct(product);
        productService.deleteService(productId);
        productService.findAProduct(productId,null, HttpStatus.SC_NOT_FOUND);

    }
    @Test
    public void findAllProducts(){
        String file = "testdata/productarray.json";
        ProductService productService = new ProductService();
        List<Product> products = productService.readProductList(file);
        for (int i = 0;i<products.size();i++){
        productService.saveANewProduct(products.get(i));
        }
        productService.findAllProducts(products);


    }

        URL url = getClass()
                .getClassLoader()
                .getResource(file);
        Product product = null;
        try {
             product = objectMapper.readValue(url,Product.class);
        } catch (IOException e) {
            System.out.println("File read error");
            e.printStackTrace();
        }
        System.out.println(product);
        ValidatableResponse response = given().baseUri(baseUri)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(product)
                .log().all()
        .when()
                .post("/")
        .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .assertThat().header("Location",containsString("/api/v1/products/"));
        String location = response.extract().header("Location");
        String id = location.substring(basePath.length()+1);
        System.out.println("Product id - " +id);

        ValidatableResponse getResponse = given().baseUri(baseUri).basePath(basePath).log().all()
        .when()
                .get("/"+id)
        .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_OK);

       Product resProduct = getResponse.extract().body().as(Product.class);
       product.setId(resProduct.getId());
       Assert.assertEquals(product,resProduct,"Incorrect product details");

   }
}
