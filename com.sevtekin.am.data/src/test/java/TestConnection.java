

import java.util.List;

import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.data.DataFacade;

public class TestConnection {


	//private static DataFacade dataFacade = DataFacade.getInstance();

	public static void main(String[] args) {
		try {
//			ClientConfig config = new DefaultClientConfig();
//			Client client = Client.create(config);
//			WebResource webResource = Client.create().resource(
//					"https://vm4.sevtekin.com:8443/am/service/cashentries");
//			SSLHandler sh = new SSLHandler();
//			ClientResponse response = webResource.accept("application/xml")
//					.get(ClientResponse.class);
//			int status = response.getStatus();
//			System.out.println("Status returned :" + status);
//			String text = response.getEntity(String.class);
//			System.out.println("Response returned: " + text);
			
			List<CashEntry> entries = DataFacade.getInstance().getCashEntries();
			for (CashEntry entry:entries)
				System.out.println("Entry Id: " + entry.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
