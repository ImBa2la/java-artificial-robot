/*
 *$Id$ 
 */
package org.styskin.ca.functions;

import java.util.Map;

/**
 * @author astyskin
 *
 */
public class CacheCriteria {
	
	private Criteria criteria;
	
	private double[][] X;
	
	private double[][] R;
	
	private Map<Criteria, Boolean> cached;
	
	public CacheCriteria(Criteria criteria, double[][] X) {
		this.criteria = criteria;
		this.X = X;	
		
	}
	
	public void clearCache() {
		for(Criteria c : cached.keySet()) {
			cached.put(c, false);			
		}
	}
	
	public void refreshCache() {
						
	}
	

	private void refreshCache(Criteria c) {
		if (! cached.get(c)) {
			
			if(c instanceof ComplexCriteria) {
				
			}
		}
	}
}
