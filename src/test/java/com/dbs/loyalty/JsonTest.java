package com.dbs.loyalty;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.dbs.loyalty.service.TadaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void generate() throws IOException {
		String json = "{\"id\":8696,\"orderNumber\":\"TADA19081615514006\",\"orderType\":\"Normal\",\"orderReference\":\"DBS19081600000002\",\"total\":50000,\"totalAll\":58000,\"notes\":null,\"status\":\"new\",\"Recipient\":{\"id\":5759,\"firstName\":\"Customer01\",\"lastName\":\"Customer01\",\"email\":\"customer01@dbs.com\",\"phone\":\"+62 8123456789\",\"address\":\"PT Infovesta Utama, Total Building fl. 10 Jl. Let. Jend S. Parman Kav. 106A\",\"postalCode\":\"11440\",\"cityName\":\"Jakarta Barat\",\"provinceName\":\"DKI Jakarta\",\"countryName\":\"Indonesia\",\"eddFrom\":1,\"eddTo\":3},\"OrderItems\":[{\"id\":9145,\"variantId\":236,\"sku\":\"072444190410\",\"itemName\":\"Payung MaGal\",\"variantName\":\"Payung Kuning\",\"category\":\"OMS MaGal\",\"itemType\":\"item\",\"isDigital\":false,\"image\":\"https://av-uploads.s3.amazonaws.com/inventory_stocks/inv-stock-20190410022301.jpg\",\"description\":\"<p>Payungnya warna kuning loh!</p>\\n\",\"quantity\":1,\"price\":50000,\"subtotal\":50000,\"status\":\"new\",\"mId\":6967,\"mBrand\":\"Mapo Galmaegi (MaGal) DUMMY\",\"mEmail\":null,\"deliveryType\":null}],\"Fees\":[{\"id\":4115,\"OrderId\":8696,\"name\":\"SHIPPING_FEE\",\"value\":8000,\"createdAt\":\"2019-08-16T15:51:40.271Z\",\"updatedAt\":\"2019-08-16T15:51:40.271Z\"}],\"OrderPayments\":[{\"id\":7124,\"OrderId\":8696,\"paymentType\":\"balance\",\"cardNumber\":\"3671287772281932\",\"amount\":58000,\"transactionId\":\"217421\",\"rewardType\":null,\"unitType\":\"cash\",\"conversionRate\":1,\"createdAt\":\"2019-08-16T15:51:40.277Z\",\"updatedAt\":\"2019-08-16T15:51:40.277Z\"}],\"OrderVendors\":null,\"trxDate\":\"2019-08-16T15:51:40.165Z\",\"cardNo\":\"3671287772281932\",\"merchant\":\"Your Company ALL\",\"programId\":\"1500278347236\",\"programName\":\"Your Company Integration Card\"}";
		JsonNode root = objectMapper.readTree(json);
		System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
		String orderNumber = root.path(TadaService.ORDER_NUMBER).asText();
		assertEquals(orderNumber, "TADA19081615514006");
	}
	
}
