package org.amc.game

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.util.Observer
import org.amc.util.Subject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test

import groovy.transform.TypeChecked;;

/**
 * Test for GameSubject
 * @author adrian
 *
 */
@TypeChecked
class GameSubjectTest {

	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;

	@Test
	void testConstructor() {
		GameSubject subject = new GameSubject();
		assertNotNull('List of observers shouldn\'t be null', subject.observerList);
		assertTrue('List should be empty', subject.observerList.empty);
	}

	@Test
	void testAttachObserver() {
		GameSubject subject = new GameSubject();
		assertTrue('List should be empty', subject.observerList.empty);
		subject.attachObserver(new Observer1());
		assertFalse('List should be empty', subject.observerList.empty);
	}

	@Test
	void testNotifyObservers() {
		GameSubject subject = new GameSubject();
		Observer observer = mock(Observer.class);
		subject.attachObserver(observer);

		def message = 'Hello';

		subject.notifyObservers(message);

		verify(observer, times(ONE)).update(subject, message);
	}

	@Test
	void testGetNoOfObservers() {
		GameSubject subject = new GameSubject();
		assertEquals(0, subject.getNoOfObservers());
		subject.attachObserver(new Observer1());
		assertEquals(ONE, subject.getNoOfObservers());
	}

	@Test
	void testRemoveObserver() {
		GameSubject subject = new GameSubject();
		Observer observer = new Observer2();
		subject.attachObserver(new Observer1());
		subject.attachObserver(observer);
		assertEquals(TWO, subject.getNoOfObservers());
		subject.removeObserver(observer);
		assertEquals(ONE, subject.getNoOfObservers());
	}

	@Test
	void testRemoveAllObservers() {
		GameSubject subject = new GameSubject();
		subject.attachObserver(new Observer1());
		subject.attachObserver(new Observer2());
		assertFalse('List of observers shouldn\'t be empty', subject.observerList.empty)
		subject.removeAllObservers();
		assertTrue('List of observers should be empty', subject.observerList.empty)
	}

	@Test
	void saveObserversTest() {
		final def separator = '|';
		List<Observer> observers = [
			new Observer1(),
			new Observer2()
		];
		def observerString = GameSubject.saveObservers(observers);
		def expectedString = "${Observer1.class.simpleName}${separator}${Observer2.class.simpleName}";
		assert observerString == expectedString;
		assertEquals(expectedString.toString(), observerString);
	}

	@Test
	void loadObserversTest() {
		final def observerStr = 'Observer1|Observer2';
		List<Observer> observers = GameSubject.loadObservers(observerStr);
		assert observers.isEmpty();
		assertTrue('The list should be empty but is not', observers.isEmpty());
	}

	/**
	 * Observer class used for testing
	 */
	private static class Observer1 implements Observer {

		@Override
		void update(Subject subject, Object message) {
			// do nothing

		}
	}

	/**
	 * Observer class used for testing
	 */
	private static class Observer2 implements Observer {

		@Override
		void update(Subject subject, Object message) {
			// do nothing

		}
	}
}
