package org.supercsv.cellprocessor;



	import org.supercsv.cellprocessor.CellProcessorAdaptor;
	import org.supercsv.cellprocessor.ift.CellProcessor;
	import org.supercsv.exception.SuperCsvCellProcessorException;
	import org.supercsv.util.CsvContext;

	/**
	 * Custom cell processor that removes commas first (as used to separate thousands) before calling
	 * the standard ParseDouble CellProcessor
	 */
	public class ParseDouble2 extends CellProcessorAdaptor {
	        
	        public ParseDouble2() {
	                super();
	        }
	        
	        public ParseDouble2(CellProcessor next) {
	                // this constructor allows other processors to be chained after ParseDouble2
	                super(next);
	        }
	        
	        public Object execute(Object value, CsvContext context) {
	                
	                validateInputNotNull(value, context);  // throws an Exception if the input is null
	                
	                // remove commas
	                String newValue = new String( value.toString() );
	                newValue = newValue.replaceAll(",","");
	                
	                // call standard cell parseDouble() CellProcessor
	                ParseDouble parseDouble = new ParseDouble();
	                
	                Object obj;
					try {
						obj = parseDouble.execute(newValue, context);
					} catch (Exception e) {
						throw new SuperCsvCellProcessorException(
		                        String.format("Could not parse '%s' as a Double2", value), context, this);
					}
	                return obj;
	        }
	}
	

