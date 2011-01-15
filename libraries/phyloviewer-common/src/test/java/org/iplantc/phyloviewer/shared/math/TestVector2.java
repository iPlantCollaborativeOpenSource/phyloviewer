package org.iplantc.phyloviewer.shared.math;

import org.junit.Test;

import junit.framework.TestCase;

public class TestVector2 extends TestCase {

	double delta = 10E-14;
	
	@Test
	public void testSubtract() {
		Vector2 v0 = new Vector2(1.0,1.0);
		Vector2 v1 = new Vector2(0.5,0.5);
		
		Vector2 answer = v0.subtract(v1);
		
		assertEquals(answer.getX(), 0.5, delta);
		assertEquals(answer.getY(), 0.5, delta);
	}
	
	@Test
	public void testAdd() {
		Vector2 v0 = new Vector2(1.0,1.0);
		Vector2 v1 = new Vector2(0.5,0.5);
		
		Vector2 answer = v0.add(v1);
		
		assertEquals(answer.getX(), 1.5, delta);
		assertEquals(answer.getY(), 1.5, delta);
	}
	
	@Test
	public void testRotate() {
		Vector2 v0 = new Vector2(1.0,0.0);
		Vector2 answer = v0.rotate(Math.PI / 2.0);
		
		assertEquals(0.0, answer.getX(), delta);
		assertEquals(1.0, answer.getY(), delta);
	}
}
