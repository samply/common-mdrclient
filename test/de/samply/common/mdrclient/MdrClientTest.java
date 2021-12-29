
package de.samply.common.mdrclient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import de.samply.common.mdrclient.domain.Catalogue;
import de.samply.common.mdrclient.domain.Code;
import de.samply.common.mdrclient.domain.CodePredicate;
import de.samply.common.mdrclient.domain.DataElement;
import de.samply.common.mdrclient.domain.Definition;
import de.samply.common.mdrclient.domain.EnumElementType;
import de.samply.common.mdrclient.domain.Label;
import de.samply.common.mdrclient.domain.Result;
import de.samply.common.mdrclient.domain.Slot;
import de.samply.common.mdrclient.domain.SlotNamePredicate;
import de.samply.common.mdrclient.domain.Validations;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An MDR client test.
 *
 * @author diogo
 */
public class MdrClientTest {

  private final static Logger logger = LoggerFactory.getLogger(MdrClientTest.class);

  /**
   * The language to use on the MDR calls.
   */
  private static final String MDR_LANGUAGE = "de";

  /**
   * MDR client initialisation for a test server.
   *
   * TODO: do not use external dependencies in unit tests
   */
  private final MdrClient mdrClient = new MdrClient("https://mdr.ccpit.dktk.dkfz.de/v3/api/mdr/");

  /**
   * Test a valid request.
   */
  @Test
  public final void testNamespaceMembersRequest() {
    String response = null;
    try {
      response = mdrClient.getJsonNamespaceMembers(MDR_LANGUAGE, null, null, "dktk");
      logger.info(response);

      List<Result> elements = mdrClient.getNamespaceMembers(MDR_LANGUAGE, "dktk");
      for (Result e : elements) {
        logger.info(e.getId() + " " + e.getDesignations().get(0).getDesignation());
      }
    } catch (Exception e) {
      fail("Connection failed.");
    }

    logger.info(response);
  }

  /**
   * Test a valid request.
   */
  @Test
  public final void testDataElementValidations() {
    // query for a data element type for validations
    try {
      String response = mdrClient
          .getJsonDataElementValidations("urn:dktk:dataelement:49:1", MDR_LANGUAGE,
              null, null);
      logger.info(response);

      Validations dataElementValidations = mdrClient
          .getDataElementValidations("urn:dktk:dataelement:49:1",
              MDR_LANGUAGE);

      logger.info("validation type: " + dataElementValidations.getDatatype());
      logger.info("validation data: " + dataElementValidations.getValidationData());
    } catch (MdrConnectionException | MdrInvalidResponseException | ExecutionException e) {
      fail("An exception occurred for a valid request.");
    }
  }

  /**
   * Test a valid request.
   */
  @Test
  public final void testDataElementCatalogue() {
    // query for a data element type for validations
    try {
      String response = mdrClient
          .getJsonDataElementCatalogue("urn:osse-6:dataelement:17:2", MDR_LANGUAGE,
              null, null);
      logger.info(response);

      Catalogue catalogue = mdrClient
          .getDataElementCatalogue("urn:osse-6:dataelement:17:2", MDR_LANGUAGE);

      logger.info("Root element ID: " + catalogue.getRoot().getIdentification().getUrn());
      logger.info("Root element designation: "
          + catalogue.getRoot().getDesignations().get(0).getDesignation());

      for (Code c : catalogue.getCodes()) {
        logger.info("Code: " + c.getIdentification().getUrn());
        logger.info("Code: " + c.getDesignations().get(0).getDesignation());
      }
    } catch (MdrConnectionException | MdrInvalidResponseException | ExecutionException e) {
      fail("An exception occurred for a valid request.");
    }
  }

  /**
   * Test a valid request.
   */
  @Test
  public final void testCatalogue() {
    // query for a data element type for validations
    try {
      String response = mdrClient.getJsonCatalogue("urn:dktk:catalog:1:1", MDR_LANGUAGE,
          null, null);
      logger.info(response);

      Catalogue catalogue = mdrClient.getCatalogue("urn:dktk:catalog:1:1", MDR_LANGUAGE);

      logger.info("Root element ID: " + catalogue.getRoot().getIdentification().getUrn());
      logger.info("Root element designation: "
          + catalogue.getRoot().getDesignations().get(0).getDesignation());

      for (String subCode : catalogue.getRoot().getSubCodes()) {
        logger.info("SubCode: " + subCode);
        Optional<Code> result = Iterables.tryFind(catalogue.getCodes(), new CodePredicate(subCode));
        if (result.isPresent()) {
          visitCodeNode(result.get(), catalogue);
        } else {
          logger.info("-- urn not found!");
        }

      }
    } catch (MdrConnectionException | MdrInvalidResponseException | ExecutionException e) {
      fail("An exception occurred for a valid request.");
    }
  }

  private void visitCodeNode(Code code, Catalogue catalogue) {
    String DKTK_SEARCH_SLOTNAME = "DKTK_SEARCH";
    logger.info("#########");
    logger.info(code.getIdentification().getUrn());
    logger.info(code.getDesignations().get(0).getDesignation());
    List<Object> subCodes = code.getSubCodes();
    if (subCodes != null && subCodes.size() > 0) {
      for (Object obj : subCodes) {
        Optional<Code> result = Iterables
            .tryFind(catalogue.getCodes(), new CodePredicate((String) obj));
        if (result.isPresent()) {
          visitCodeNode(result.get(), catalogue);
        }
      }
    } else {
      try {
        ArrayList<Slot> slots = mdrClient
            .getCodeSlots(catalogue.getRoot().getIdentification().getUrn(),
                code.getIdentification().getUrn());
        Optional<Slot> slot = Iterables.tryFind(slots, new SlotNamePredicate(DKTK_SEARCH_SLOTNAME));
        if (slot.isPresent()) {
          logger.info(slot.get().getSlotValue());
        } else {
          logger.info("Slot not found!");
        }
      } catch (MdrConnectionException | MdrInvalidResponseException | ExecutionException e) {
        //fail("Couldn't get slots");
        logger.warn("no slot found...");
      }
    }
  }

  /**
   * Test a valid request.
   */
  @Test
  public final void testCode() {
    // query for a data element type for validations
    try {
      String response = mdrClient
          .getJsonCode("urn:osse-6:catalog:1:1", "urn:osse-6:code:263:1", null, null);
      System.out.println(response);

      Code code = mdrClient.getCode("urn:osse-6:catalog:1:1", "urn:osse-6:code:263:1");

      System.out.println("code of the code: " + code.getCode());
      System.out.println("Urn of the code: " + code.getIdentification().getUrn());
    } catch (MdrConnectionException | MdrInvalidResponseException | ExecutionException e) {
      fail("An exception occurred for a valid request.");
    }
  }

  /**
   * Test multiple valid request.
   */
  @Test
  public final void multipleMdrQueries() {
    String response;
    try {
      response = mdrClient.getJsonNamespaceMembers(MDR_LANGUAGE, null, null, "dktk");
      logger.info(response);

      List<Result> elements = mdrClient.getNamespaceMembers(MDR_LANGUAGE, "dktk");
      for (Result e : elements) {
        logger.info(e.getId() + " " + e.getDesignations().get(0).getDesignation());
      }

      // query for all elements in a root group
      List<Result> rootGroupMembers = mdrClient.getMembers(elements.get(0).getId(), MDR_LANGUAGE);
      for (Result r : rootGroupMembers) {
        logger.info(r.getId() + " " + r.getDesignations().get(0).getDesignation());
      }

      Result group = null;
      for (Result r : rootGroupMembers) {
        if (r.getType().compareTo(EnumElementType.DATAELEMENTGROUP.name()) == 0) {
          group = r;
        }
      }
      if (group != null) {
        logger.info("Getting data elements from group (belonging to root group)" + group.getId()
            + "...");
        List<Result> resultList = mdrClient.getMembers(group.getId(), MDR_LANGUAGE);
        for (Result r : resultList) {
          logger.info(r.getId() + " " + r.getDesignations().get(0).getDesignation());
        }
      } else {
        logger.warn("Could not find any elemens from a group in a root group");
      }

      // query for the definition of a data element
      response = mdrClient.getJsonDataElementDefinition("urn:dktk:dataelement:49:1", //$NON-NLS-1$
          MDR_LANGUAGE, null, null);
      logger.info(response);

      Definition dataElementDefinition = mdrClient.getDataElementDefinition(
          "urn:dktk:dataelement:56:2", MDR_LANGUAGE); //$NON-NLS-1$
      logger.info(dataElementDefinition.getDesignations().get(0).getDesignation()
          + "- " + dataElementDefinition.getDesignations().get(0).getDefinition()); //$NON-NLS-1$

      dataElementDefinition = mdrClient
          .getDataElementDefinition("urn:dktk:dataelement:50:1", MDR_LANGUAGE);
      logger.info(dataElementDefinition.getDesignations().get(0).getDesignation()
          + "- " + dataElementDefinition.getDesignations().get(0).getDefinition()); //$NON-NLS-1$

      // query a record information
      logger.info("Querying a record...");
      List<Label> recordLabel = mdrClient.getRecordLabel("urn:osse-3:record:4:1", MDR_LANGUAGE);
      logger.info("Record label: " + recordLabel.get(0).getDefinition());
      List<Result> resultList = mdrClient.getRecordMembers("urn:osse-3:record:4:1", MDR_LANGUAGE);
      for (Result r : resultList) {
        logger.info("Record member: " + r.getId() + " "
            + r.getDesignations().get(0).getDesignation());
      }

      // search for text
      logger.info("Searching for some expression...");
      resultList = mdrClient.search("date", MDR_LANGUAGE, null);
      for (Result r : resultList) {
        logger.info(r.getId() + " " + r.getDesignations().get(0).getDesignation());
      }

      // checking slots
      logger.info("Checking slots...");
      ArrayList<Slot> slots = mdrClient.getDataElementSlots("urn:osse-ror:dataelement:51:1");
      for (Slot s : slots) {
        logger.info("Slot: " + s.getSlotName() + ", " + s.getSlotValue());
      }
    } catch (ExecutionException | MdrConnectionException | MdrInvalidResponseException e1) {
      fail("An error occurred for valid MDR requests.");
    }
  }

  @Test
  public final void testGetDataElement() {
    try {
      final Stopwatch stopwatch = Stopwatch.createStarted();
      DataElement dataElement = mdrClient.getDataElement("urn:dktk:dataelement:26:latest", "en");
      logger.info(
          "First call execution time: " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + " "
              + TimeUnit.MILLISECONDS.name());
      stopwatch.reset().start();
      dataElement = mdrClient.getDataElement("urn:dktk:dataelement:26:latest", "en");
      logger.info(
          "Second call execution time: " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + " "
              + TimeUnit.MILLISECONDS.name());
      stopwatch.reset().start();
      dataElement = mdrClient.getDataElement("urn:dktk:dataelement:26:latest", "en");
      logger.info(
          "Third call execution time: " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + " "
              + TimeUnit.MILLISECONDS.name());
      mdrClient.cleanCache();
      stopwatch.reset().start();
      dataElement = mdrClient.getDataElement("urn:dktk:dataelement:26:latest", "en");
      logger.info("First call after cleaning cache execution time: " + stopwatch.stop()
          .elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.name());

      assertNotNull(dataElement);
      assertNotNull(dataElement.getDesignations());
      assertNotNull(dataElement.getIdentification());
      assertNotNull(dataElement.getSlots());
      assertNotNull(dataElement.getValidation());
    } catch (MdrConnectionException | MdrInvalidResponseException | ExecutionException e) {
      fail("An Exception was caught: " + e.getMessage());
    }
  }
}
