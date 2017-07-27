package com.sevtekin.am.service;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sevtekin.am.common.CashEntries;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntries;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntries;
import com.sevtekin.am.common.KeywordEntry;
import com.sevtekin.am.common.OwnerEntries;
import com.sevtekin.am.common.OwnerEntry;
import com.sevtekin.am.common.SnapshotEntries;
import com.sevtekin.am.common.SnapshotEntry;
import com.sevtekin.am.common.config.ConfigReader;
import com.sevtekin.am.common.config.EncryptionHandler;
import com.sevtekin.am.data.DataFacade;

@Path("/")
@Produces("application/xml")
public class AMResource {
	private static DataFacade dataFacade = null;
	private static AMResourceHelper helper = null;

	public AMResource() {
		dataFacade = DataFacade.getInstance();
		helper = AMResourceHelper.getInstance();
	}

	// CASH

	@GET
	@Path("cashentries")
	public Response getCashEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /CASHENTRIES RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /CASHENTRIES RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntries entries = new CashEntries();
		entries.setCashEntry(dataFacade.getCashEntries());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("estimateentries")
	public Response getEstimateEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /ESTIMATEENTRIES RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /ESTIMATEENTRIES RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntries entries = new CashEntries();
		entries.setCashEntry(dataFacade.getEstimateEntries());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("cashentries/{filters}")
	public Response getCashEntries(@PathParam("filters") String filters, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /CASHENTRIES{FILTERS} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /CASHENTRIES{FILTERS} RESOURCE ACCESS GRANTED TO " + clientIp);

		CashEntries entries = new CashEntries();
		String tmp1 = filters;
		System.out.println(tmp1);
		String tmp2 = tmp1.substring(18, tmp1.indexOf(" and"));
		tmp1 = tmp1.substring(tmp1.indexOf(" and"), tmp1.length());
		tmp1 = " and description like '%" + tmp2 + "%'" + tmp1;
		// System.out.println(tmp1);
		entries.setCashEntry(dataFacade.getCashEntries(tmp1));
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("reports/sumbymonth")
	public Response getCashEntriesSumByMonth(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /REPORTS/SUMBYMONTH RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /REPORTS/SUMBYMONTH RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntries entries = new CashEntries();
		entries.setCashEntry(dataFacade.getCashEntriesSumByMonth());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("reports/sumbyowner")
	public Response getCashEntriesSumByOwner(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /REPORTS/SUMBYOWNER RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /REPORTS/SUMBYOWNER RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntries entries = new CashEntries();
		entries.setCashEntry(dataFacade.getCashEntriesSumByOwner());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("reports/sumbyyear/{year}")
	public Response getCashEntriesSumByOwner(@PathParam("year") int year, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /REPORTS/SUMBYYEAR RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /REPORTS/SUMBYYEAR RESOURCE ACCESS GRANTED TO " + clientIp);
		double result = dataFacade.getCashEntriesSumByYear(year);
		CashEntry entry = new CashEntry();
		entry.setAmount(result);
		return Response.ok(entry, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("reports/sumbymonthbycategory")
	public Response getCashEntriesSumByMonthByCategory(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /REPORTS/SUMBYCATEGORY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /REPORTS/SUMBYCATEGORY RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntries entries = new CashEntries();
		entries.setCashEntry(dataFacade.getCashEntriesSumByMonthByCategory());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("reports/top10bycategory/{year}/{sort}")
	public Response getTop5ByCategory(@PathParam("year") int year, @PathParam("sort") String sort,
			@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /REPORTS/TOP5BYCATEGORY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /REPORTS/TOP5BYCATEGORY RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntries entries = new CashEntries();
		entries.setCashEntry(dataFacade.getCashEntriesSumByMonthByCategory(year, sort));
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("cashentry/{id}")
	public Response getCashEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /CASHENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /CASHENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		CashEntry entry = dataFacade.getCashEntry(id);
		return Response.ok(entry, MediaType.APPLICATION_XML).build();
	}

	@POST
	@Path("addcashentry")
	@Consumes("application/xml")
	public Response addCashEntry(CashEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /ADDCASHENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /ADDCASHENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.addCashEntry(entry);
		} catch (Exception e) {
			System.out.println(
					new Timestamp(new Date().getTime()) + " [AM SERVICE][ERROR] Problem occured adding the cash entry");
			e.printStackTrace();
		}
		return Response.ok(entry, MediaType.APPLICATION_XML).build();
	}

	@POST
	@Path("deletecashentry/{id}")
	public Response deleteCashEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /DELETECASHENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /DELETECASHENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.deleteCashEntry(id);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured deleting the cash entry");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("updatecashentry")
	@Consumes("application/xml")
	public Response updateCashEntry(CashEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /UPDATECASHENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /UPDATECASHENTRY RESOURCE ACCESS GRANTED TO " + clientIp);

		try {
			dataFacade.updateCashEntry(entry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured updating the cash entry ");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("updateestimateentry")
	@Consumes("application/xml")
	public Response updateEstimateEntry(CashEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /UPDATEESTIMATEENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /UPDATEESTIMATEENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.updateEstimateEntry(entry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured updating the estimate entry ");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	// CATEGORY

	@GET
	@Path("categoryentries")
	public Response getCategoryEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /CATEGORYENTRIES RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /CATEGORYENTRIES RESOURCE ACCESS GRANTED TO " + clientIp);
		CategoryEntries entries = new CategoryEntries();
		entries.setCategoryEntry(dataFacade.getCategoryEntries());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("categoryentry/{id}")
	public Response getCategoryEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /CATEGORYENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /CATEGORYENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		CategoryEntry entry = dataFacade.getCategoryEntry(id);
		return Response.ok(entry, MediaType.APPLICATION_XML).build();
	}

	@POST
	@Path("addcategoryentry")
	@Consumes("application/xml")
	public Response addCategoryEntry(CategoryEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /ADDCATEGORYENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /ADDCATEGORYENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		dataFacade.addCategoryEntry(entry);
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("updatecategoryentry")
	@Consumes("application/xml")
	public Response updateCategoryEntry(CategoryEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /UPDATECATEGORYENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /UPDATECATEGORYENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.updateCategoryEntry(entry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured updating the category entry ");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("deletecategoryentry/{id}")
	public Response deleteCategoryEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /DELETECATEGORYENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /DELETECATEGORYENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.deleteCategoryEntry(id);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured deleting the category entry");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	// KEYWORD

	@GET
	@Path("keywordentries")
	public Response getKeywordEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /KEYWORDENTRIES RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /KEYWORDENTRIES RESOURCE ACCESS GRANTED TO " + clientIp);
		KeywordEntries entries = new KeywordEntries();
		entries.setKeywordEntry(dataFacade.getKeywordEntries());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("keywordentry/{id}")
	public Response getKeywordEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /KEYWORDENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /KEYWORDENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		KeywordEntry entry = dataFacade.getKeywordEntry(id);
		return Response.ok(entry, MediaType.APPLICATION_XML).build();
	}

	@POST
	@Path("addkeywordentry")
	@Consumes("application/xml")
	@Produces("text/html")
	public Response addKeywordEntry(KeywordEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /ADDKEYWORDENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /ADDKEYWORDENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		List<KeywordEntry> entries = dataFacade.getKeywordEntries();
		String ret = "OK";
		for (KeywordEntry key : entries)
			if (key.getName().toUpperCase().contains(entry.getName().toUpperCase()))
				ret = "INCLUDED";
			else if (entry.getName().toUpperCase().contains(key.getName().toUpperCase()))
				ret = "INCLUDES";
		if (ret.equals("OK"))
			dataFacade.addKeywordEntry(entry);
		return Response.ok(ret, MediaType.TEXT_HTML).build();
	}

	@POST
	@Path("updatekeywordentry")
	@Consumes("application/xml")
	public Response updateKeywordEntry(KeywordEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /UPDATEKEYWORDENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /UPDATEKEYWORDENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.updateKeywordEntry(entry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured updating the keyword entry ");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("deletekeywordentry/{id}")
	public Response deleteKeywordEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /DELETEKEYWORDENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /DELETEKEYWORDENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.deleteKeywordEntry(id);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured deleting the keyword entry");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	// OWNER

	@GET
	@Path("ownerentries")
	public Response getOwnerEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		/*if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /OWNERENTRIES RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}*/
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /OWNERENTRIES RESOURCE ACCESS GRANTED TO " + clientIp);
		OwnerEntries entries = new OwnerEntries();
		entries.setOwnerEntry(dataFacade.getOwnerEntries());
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Path("ownerentry/{id}")
	public Response getOwnerEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /OWNERENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /OWNERENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		OwnerEntry entry = dataFacade.getOwnerEntry(id);
		return Response.ok(entry, MediaType.APPLICATION_XML).build();
	}

	@POST
	@Path("addownerentry")
	@Consumes("application/xml")
	public Response addOwnerEntry(OwnerEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /ADDOWNERENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /ADDOWNERENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		dataFacade.addOwnerEntry(entry);
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("updateownerentry")
	@Consumes("application/xml")
	public Response updateOwnerEntry(OwnerEntry entry, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /UPDATEOWNERENTRY RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /UPDATEOWNERENTRY RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.updateOwnerEntry(entry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured updating the owner entry ");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	@POST
	@Path("deleteownerentry/{id}")
	public Response deleteOwnerEntry(@PathParam("id") int id, @Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /DELETEOWNERENTRY/{ID} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /DELETEOWNERENTRY/{ID} RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			dataFacade.deleteOwnerEntry(id);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured deleting the owner entry");
			e.printStackTrace();
		}
		return Response.status(Status.ACCEPTED).build();
	}

	// PROPERTIES FILE

	@GET
	@Path("encryptpropfile")
	@Produces("text/html")
	public Response encryptFile(@Context HttpServletRequest request) {
		System.out.println("STARTING SOMETHING");
		ConfigReader configReader = new ConfigReader();
		// String clientIp = request.getRemoteAddr();
		// System.out.println("FROM IP:" + clientIp);
		// if (!configReader.getAllowedConsumerList().contains(clientIp)) {
		// System.out.println(new Timestamp(new Date().getTime())
		// + "[AM SERVICE][WARNING] /ENCRYPTPROPFILE RESOURCE ACCESS DENIED TO " +
		// clientIp);
		// return Response.status(Status.FORBIDDEN).build();
		// }
		// System.out.println(new Timestamp(new Date().getTime())
		// + "[AM SERVICE][INFO] /ENCRYPTPROPFILE RESOURCE ACCESS GRANTED TO " +
		// clientIp);
		String ret = "PROPERTIES FILE SUCCESSFULLY ENCRYPTED";
		try {
			new EncryptionHandler().encryptPropFile();
		} catch (Exception e) {
			ret = e.getMessage();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured encrypting the properties file");
			e.printStackTrace();
		}
		return Response.ok(ret, MediaType.TEXT_HTML).build();
	}

	@GET
	@Path("decryptpropfile")
	@Produces("text/html")
	public Response decryptFile(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		/*
		 * if (!configReader.getAllowedConsumerList().contains(clientIp)) {
		 * System.out.println(new Timestamp(new Date().getTime()) +
		 * "[AM SERVICE][WARNING] /DECRYPTPROPFILE RESOURCE ACCESS DENIED TO " +
		 * clientIp); return Response.status(Status.FORBIDDEN).build(); }
		 */
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /DECRYPTPROPFILE RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			new EncryptionHandler().decryptPropFile();
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM SERVICE][ERROR] Problem occured decrypting the properties file");
			e.printStackTrace();
		}
		String ret = "PROPERTIES FILE SUCCESSFULLY DECRYPTED";
		return Response.ok(ret, MediaType.TEXT_HTML).build();
	}

	// SNAPHOSTS

	@POST
	@Path("takesnapshot/{type}")
	@Produces("text/html")
	public Response takeSnapShot(@PathParam("type") String type, @Context HttpServletRequest request) {
		String msg = "";
		ConfigReader configReader = new ConfigReader();
		String db = configReader.getDBName();
		String dbhost = configReader.getDBHost();
		String dbport = configReader.getDBPort();
		String user = configReader.getDBUser();
		String password = configReader.getDBPassword();
		String location = configReader.getDBDumpLocation();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /TAKESNAPSHOT RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /TAKESNAPHOT RESOURCE ACCESS GRANTED TO " + clientIp);
		Process p = null;

		try {
			File f = null;
			DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Runtime runtime = Runtime.getRuntime();
			String fileName = sdf.format(new Date()) + type + ".sql";
			f = new File(location + "/" + fileName);
			System.out.println();
			if (!f.exists()) {
				p = runtime.exec(new String[] { "bash", "-c", "mysqldump -h " + dbhost + " --port " + dbport + " -u "
						+ user + " -p" + password + " " + db + " > " + location + "/" + fileName });

				int processComplete = p.waitFor();
				if (processComplete == 0)
					msg = new Timestamp(new Date().getTime()) + " [AM SERVICE][INFO] Backup created successfully!";
				else
					msg = new Timestamp(new Date().getTime()) + " [AM SERVICE][ERROR] Could not create the backup";
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(msg, MediaType.TEXT_HTML).build();
	}

	@POST
	@Path("restoresnapshot/{snapshot}")
	@Produces("text/html")
	public Response restoreSnapShot(@PathParam("snapshot") String snapshot, @Context HttpServletRequest request) {
		String msg = "";
		ConfigReader configReader = new ConfigReader();
		String db = configReader.getDBName();
		String dbhost = configReader.getDBHost();
		String dbport = configReader.getDBPort();
		String user = configReader.getDBUser();
		String password = configReader.getDBPassword();
		String location = configReader.getDBDumpLocation();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /RESTORESNAPSHOT RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /RESTORESNAPHOT RESOURCE ACCESS GRANTED TO " + clientIp);
		Process p = null;

		try {
			Runtime runtime = Runtime.getRuntime();
			File f = new File(location + "/" + snapshot);
			if (f.exists()) {
				System.out.println("mysql -h " + dbhost + " -u " + user + " -p" + password + " " + db + " < " + location
						+ "/" + snapshot);
				p = runtime.exec(new String[] { "bash", "-c", "mysql -h " + dbhost + " --port " + dbport + " -u " + user
						+ " -p" + password + " " + db + " < " + location + "/" + snapshot });
				int processComplete = p.waitFor();
				if (processComplete == 0)
					msg = new Timestamp(new Date().getTime()) + " [AM SERVICE][INFO] Backup restored successfully!";
				else
					msg = new Timestamp(new Date().getTime()) + " [AM SERVICE][ERROR] Could not restore the backup";
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(msg, MediaType.TEXT_HTML).build();
	}

	@GET
	@Path("snapshotentries")
	@Produces("application/xml")
	public Response getSnapshotEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /SNAPHOTENTRIES RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /SNAPHOTENTRIES RESOURCE ACCESS GRANTED TO " + clientIp);
		SnapshotEntries entries = new SnapshotEntries();
		List<SnapshotEntry> files = new ArrayList<SnapshotEntry>();
		String location = configReader.getDBDumpLocation();
		try {
			File folder = new File(location);
			int daysOld = 90;
			if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date today = new Date();
				System.out.println(sdf.format(today));
				for (File listFile : listOfFiles) {
					String tmp = listFile.getName().substring(0, listFile.getName().length() - 6);
					Date fileDate = sdf.parse(tmp);
					long diff = (today.getTime() - fileDate.getTime()) / (1000 * 60 * 60 * 24);
					System.out.println(listFile + " is " + diff + " days old");
					if (diff >= 90) {
						if (listFile.isFile()) {
							listFile.delete();
							System.out.println("File needs to be deleted " + listFile);
						}
					}
				}
			}
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++)
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("sql")) {
					SnapshotEntry entry = new SnapshotEntry();
					entry.setName(listOfFiles[i].getName());
					entry.setId(i);
					files.add(entry);
				}
			entries.setSnapshotEntry(files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(entries, MediaType.APPLICATION_XML).build();
	}

	@POST
	@Path("deletesnapshotentry/{snapshot}")
	@Produces("text/html")
	public Response deleteSnapShotEntry(@PathParam("snapshot") String snapshot, @Context HttpServletRequest request) {
		String msg = "";
		ConfigReader configReader = new ConfigReader();
		String location = configReader.getDBDumpLocation();
		String clientIp = request.getRemoteAddr();
		if (!configReader.getAllowedConsumerList().contains(clientIp)) {
			System.out.println(new Timestamp(new Date().getTime())
					+ "[AM SERVICE][WARNING] /DELETESNAPSHOTENTRY/{SNAPSHOT} RESOURCE ACCESS DENIED TO " + clientIp);
			return Response.status(Status.FORBIDDEN).build();
		}
		System.out.println(new Timestamp(new Date().getTime())
				+ "[AM SERVICE][INFO] /DELETESNAPSHOTENTRY/{SNAPSHOT} RESOURCE ACCESS GRANTED TO " + clientIp);
		try {
			File folder = new File(location);
			File[] listOfFiles = folder.listFiles();
			File f = null;
			for (int i = 0; i < listOfFiles.length; i++)
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().equalsIgnoreCase(snapshot)) {
					f = new File(location + "/" + listOfFiles[i].getName());
					f.delete();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(msg, MediaType.TEXT_HTML).build();
	}

	//////////////////// JSON v2 API //////////////////////

	@GET
	@Path("v2/jsontest")
	@Produces("application/json")
	public Response jsonTest() {
		System.out.println(new Timestamp(new Date().getTime()) + "[AM SERVICE][INFO] jsontest requested");
		String jsonStr = "{\"balance\": 1000.21, \"name\":\"foo\"}";
		return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("v2/t")
	@Produces("application/json")
	public Response t1() {
		System.out.println(new Timestamp(new Date().getTime()) + "[AM SERVICE][INFO] jsontest requested");
		String jsonStr = "{\"balance\": 1000.21, \"name\":\"foo\"}";
		return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/jsonarraytest")
	@Produces("application/json")
	public Response jsonArrayTest(@Context HttpServletRequest request) {
		System.out.println(new Timestamp(new Date().getTime()) + "[AM SERVICE][INFO] jsonarraytest requested");
		String jsonStr = "\"employees\":[{\"firstName\":\"John\", \"lastName\":\"Doe\"},"
				+ "{\"firstName\":\"Anna\", \"lastName\":\"Smith\"}]";
		return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("v2/jsonarraytest2")
	@Produces("application/json")
	public Response jsonArrayTest2(@Context HttpServletRequest request) {
		System.out.println(new Timestamp(new Date().getTime()) + "[AM SERVICE][INFO] jsonarraytest requested");
		String jsonStr = "\"employees\":[{\"firstName\":\"John\", \"lastName\":\"Doe\"},"
				+ "{\"firstName\":\"Anna\", \"lastName\":\"Smith\"}]";
		return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/snapshotentries")
	@Produces("application/json")
	public Response snapshotEntries(@Context HttpServletRequest request) {
		ConfigReader configReader = new ConfigReader();
		SnapshotEntries entries = new SnapshotEntries();
		String location = configReader.getDBDumpLocation();
		System.out.println("LOCATION = " + location);
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		try {

			int daysOld = 90;
			File folder = new File(location);
			if (folder.exists()) {
				File[] listOfFiles = folder.listFiles();
				long purgeTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000);
				for (File listFile : listOfFiles) {
					System.out.println(
							listFile + " is last modified" + listFile.lastModified() + " purge time " + purgeTime);
					if (listFile.lastModified() > purgeTime) {
						System.out.println("File or directory name: " + listFile);
						if (listFile.isFile()) {
							// listFile.delete();
							System.out.println("This is a file: " + listFile);
							System.out.println("listFile.lastModified()" + listFile.lastModified());
							System.out.println("purgeTime: " + purgeTime);
						}
					}
				}
			}

			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++)
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("sql")) {
					SnapshotEntry entry = new SnapshotEntry();
					JSONObject object = new JSONObject();
					object.put("id", i);
					object.put("name", listOfFiles[i].getName());
					objects.put(object);
				}
			result.put("entries", objects);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/cashentries")
	@Produces("application/json")
	public Response cashEntries() {
		List<CashEntry> entries = dataFacade.getCashEntries();
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		try {
			for (CashEntry entry : entries) {
				JSONObject object = new JSONObject();
				object.put("id", entry.getId());
				object.put("amount", entry.getAmount());
				object.put("category", entry.getCategoryEntry().getName());
				object.put("date", new SimpleDateFormat("yyyy/MM/dd").format(entry.getActualdate()));
				object.put("description", entry.getDescription());
				objects.put(object);
			}
			result.put("entries", objects);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/reports/sumbyowner")
	@Produces("application/json")
	public Response sumByOwner() {
		List<CashEntry> entries = dataFacade.getCashEntriesSumByOwner();
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		try {
			for (CashEntry entry : entries) {
				JSONObject object = new JSONObject();
				object.put("amount", entry.getAmount());
				object.put("account", entry.getOwnerEntry().getName());
				objects.put(object);
			}
			result.put("entries", objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/reports/top10bycategory/{year}/{sort}")
	@Produces("application/json")
	public Response top10ByCategory(@PathParam("year") int year, @PathParam("sort") String sort) {
		List<CashEntry> entries = dataFacade.getCashEntriesSumByMonthByCategory(year, sort);
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		try {
			for (CashEntry entry : entries) {
				JSONObject object = new JSONObject();
				object.put("amount", entry.getAmount());
				object.put("category", entry.getCategoryEntry().getName().toUpperCase());
				objects.put(object);
			}
			result.put("entries", objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/reports/velocity/annual")
	@Produces("application/json")
	public Response annualVelocity() {

		helper.gatherData();
		List<String> years = helper.getYears();
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		JSONObject object = new JSONObject();

		// ANNUALLY REGAINED/SPENT
		for (String year : years) {
			object = new JSONObject();
			object.put("name", year + " GAINED");
			object.put("value", helper.annuallyGained(Integer.parseInt(year)));
			objects.put(object);
			object = new JSONObject();
			object.put("name", year + " SPENT");
			object.put("value", helper.annuallySpent(Integer.parseInt(year)));
			objects.put(object);
		}

		// ANNUALLY RETAINED
		for (String year : years) {
			object = new JSONObject();
			object.put("name", year + " RETAINED");
			object.put("value", helper.annuallyRetained(Integer.parseInt(year)));
			objects.put(object);
		}

		Calendar calendar = Calendar.getInstance();

		// ANNUAL VELOCITY FOR EACH YEAR
		int yr = calendar.get(Calendar.YEAR);
		for (String year : years) {
			int yrInt = Integer.parseInt(year);
			if (yr != yrInt) {
				object = new JSONObject();
				object.put("name", year + " VELOCITY");
				object.put("value", helper.annualVelocity(Integer.parseInt(year)));
				objects.put(object);
			}
		}
		// YTD VELOCITY

		for (String year : years) {
			object = new JSONObject();
			object.put("name", year + " YTD VELOCITY");
			object.put("value", helper.velocityYTD(Integer.parseInt(year)));
			objects.put(object);
		}

		// ANNUAL VELOCITY
		object = new JSONObject();
		object.put("name", "ANNUAL VELOCITY");
		//object.put("value", helper.annualVelocity());
		objects.put(object);

		// YTD SUMS
		// for (String year : years) {
		// object = new JSONObject();
		// object.put("name", year + "-YTD");
		// object.put("value", helper.retainedYTD(Integer.parseInt(year)));
		// objects.put(object);
		// }
		// ANNUALLY RETAINED AVERAGE
		// object = new JSONObject();
		// object.put("name", "ANNUAL RETAINED AVERAGE");
		// object.put("value", helper.annualRetainedAverage());
		// objects.put(object);

		// ADD RESULTS
		result.put("entries", objects);

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/reports/velocity/monthly")
	@Produces("application/json")
	public Response monthlyVelocity() {

		helper.gatherData();
		List<String> years = helper.getYears();
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		JSONObject object = new JSONObject();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		// YTD VELOCITY

		for (String yr : years) {
			object = new JSONObject();
			object.put("name", yr + " YTD VELOCITY");
			object.put("value", helper.monthlyVelocityYTD(Integer.parseInt(yr)));
			objects.put(object);
		}

		// VELOCITY

		DateFormatSymbols dfs = new DateFormatSymbols();
		for (String yr : years) {
			object = new JSONObject();
			object.put("name", yr + " " + dfs.getShortMonths()[month].toUpperCase() + " RETAINED");
			object.put("value", helper.monthlyRetained(Integer.parseInt(yr), month));
			objects.put(object);
		}

		List<Integer> lastFew = new ArrayList<Integer>();
		if (month - 1 >= 0)
			lastFew.add(month - 1);
		if (month - 2 >= 0)
			lastFew.add(month - 2);

		for (int m : lastFew) {
			object = new JSONObject();
			object.put("name", year + " " + dfs.getShortMonths()[m].toUpperCase() + " RETAINED");
			object.put("value", helper.monthlyRetained(year, m));
			objects.put(object);
		}

		// ADD RESULTS
		result.put("entries", objects);

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("v2/encryptpropfile")
	@Produces("application/json")
	public Response encryptFile() {
		JSONObject result = new JSONObject();
		JSONArray objects = new JSONArray();
		try {
			new EncryptionHandler().encryptPropFile();
			JSONObject object = new JSONObject();
			object.put("msg", "YAY");
			objects.put(object);
			result.put("msgs", objects);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
}
