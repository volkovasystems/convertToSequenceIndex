import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

public class convertToSequenceIndex{
	private static final String DEFAULT_SEPARATOR = ",";
	private static final String EMPTY_STRING = "";

	public static void main( String... parameters ){
		try{
			String separator = parameters[ 0 ];
			String sequence = parameters[ 1 ];
			String dictionary = EMPTY_STRING;

			if( parameters.length == 3 ){
				dictionary = parameters[ 2 ];
			}else if( parameters.length == 2 ){
				separator = EMPTY_STRING;
				sequence = parameters[ 0 ];
				dictionary = parameters[ 1 ];
			}

			BigInteger sequenceIndex = convertToSequenceIndex( separator, sequence, dictionary );
			System.out.print( sequenceIndex.toString( ) );
		}catch( Exception exception ){
			System.err.print( "e: " + exception.getMessage( ) );
		}
	}

	public static BigInteger convertToSequenceIndex( String separator, String sequence, String dictionary ){
		if( separator == null || separator == EMPTY_STRING ){
			separator = DEFAULT_SEPARATOR;
		}

		/*
			We need to split the sequence and the dictionary
				so that we can use the capabilities of arrays in java.
		*/
		String sequenceList[ ] = null;
		String dictionaryList[ ] = null;
		Pattern separatorPattern = Pattern.compile( separator );
		if( contains( sequence, separatorPattern )
			&& contains( sequence, separatorPattern ) )
		{
			sequenceList = sequence.split( separator );
			dictionaryList = dictionary.split( separator );
		}else{
			/*
				If we can't find any separator then separate
					them by empty spaces.
			*/
			sequenceList = sequence.split( EMPTY_STRING );
			dictionaryList = dictionary.split( EMPTY_STRING );

			/*
				We are doing this because there's an extra 
					null element when we split by empty string.
			*/
			sequenceList = Arrays.copyOfRange( sequenceList, 1, sequenceList.length );
			dictionaryList = Arrays.copyOfRange( dictionaryList, 1, dictionaryList.length );
		}

		int sequenceLength = sequenceList.length;
		Integer dictionarySequenceLength = dictionaryList.length;
		BigInteger dictionaryLength = new BigInteger( dictionarySequenceLength.toString( ) );
		
		BigInteger sequenceIndex = BigInteger.ZERO;
		BigInteger dictionaryIndex = BigInteger.ZERO;
		
		int elementIndex = sequenceLength - 1;

		String element = EMPTY_STRING;
		Integer dictionaryElementIndex = 0;
		
		for( int index = 0; index < sequenceLength; index++, elementIndex-- ){
			element = sequenceList[ elementIndex ];

			dictionaryElementIndex = Arrays.binarySearch( dictionaryList, element );
			dictionaryIndex = new BigInteger( dictionaryElementIndex.toString( ) );
			dictionaryIndex = dictionaryIndex.add( BigInteger.ONE );

			/*
				The formula for the conversion from any sequence 
					to it's lexicographic permutated index:

					L(n,w,d) = nEi=0 (d^i)(w_n-i)

					The summation of the length of dictionary raise to the current index multiplied
						by the element index from the dictionary starting at the last element.
			*/
			sequenceIndex = sequenceIndex.add( dictionaryLength.pow( index ).multiply( dictionaryIndex ) );
		}
		return sequenceIndex;
	}

	/*
		This will check if the pattern is found in the string.
		This will be used for finding the separator in the given sequence.
		This is made private and static so that it will not be even
			accessible using reflections.
	*/
	private static boolean contains( String string, Pattern pattern ){
		Matcher matcher = pattern.matcher( string );
		int matchCount = 0;
		while( matcher.find( ) ){
			matchCount++;
			if( matchCount > 0 ){
				return true;
			}
		}
		return false;
	}
}