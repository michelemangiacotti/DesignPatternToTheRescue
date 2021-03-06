package edu.pezzati.patterns.state.status;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.pezzati.patterns.state.Status;
import edu.pezzati.patterns.state.util.OurSqlConnection;

/**
 * If C went well the whole process ends successfully: OK state. If C goes wrong try it again.
 * @author PEFR
 *
 */
public class StatusCTest {

	private Status status;
	private OurSqlConnection connection;

	@BeforeEach
	public void initEach() {
		connection = Mockito.mock(OurSqlConnection.class);
		status = new StatusC();
		status.setConnection(connection);
	}
	
	@Test
	public void ifSomethingGoesWrongStatusCEvolvesInStatusCRetry() throws Exception {
		Status expected = new StatusCRetry();
		expected.setConnection(connection);
		Mockito.doThrow(new Exception()).when(connection).doSomethingThatCanGoWrong();
		Status actual = status.next();
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void ifEverythingGoesFineStatusCEvolvesInStatusOK() throws Exception {
		Mockito.doNothing().when(connection).doSomethingThatCanGoWrong();
		Status actual = status.next();
		MatcherAssert.assertThat(actual, IsInstanceOf.instanceOf(StatusOK.class));
	}
}
