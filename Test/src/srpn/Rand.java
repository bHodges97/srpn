package srpn;

/**
 * This class creates a pseudo randomly generated number. 
 * To mimic srpn.lcpu as closely as possible, this class reimplements the rand function from gcc in Java.
 * The original implementation of rand() can be found in gcc's source code.
 * The linear congruential generator was removed as srpn.lcpu only uses the trinomial one.
 * <br>
 * I'm not sure if this was required from the coursework although if the srpn did set a seed for R.N.G this would have been near impossible.
 * Also java.util.Random uses a different  linear congruential pseudorandom generator which is mentioned in the JavaDoc for Random.next().
 * @see <a href="https://sourceware.org/git/?p=glibc.git;a=blob;f=stdlib/random_r.c;hb=glibc-2.15#l361"> random_r.c source</a>
 * @see <a href="https://sourceware.org/git/?p=glibc.git;a=blob;f=stdlib/random.c;hb=glibc-2.15#l292"> random.c source</a>

 *
 */
public class Rand {
	//The variable TYPE_3 is redundant 
	private static int TYPE_3 = 3;

	private static int randtbl[] = { TYPE_3, -1726662223, 379960547, 1735697613, 1040273694, 1313901226, 1627687941, -179304937,
			-2073333483, 1780058412, -1989503057, -615974602, 344556628, 939512070, -1249116260, 1507946756, -812545463,
			154635395, 1388815473, -1926676823, 525320961, -1009028674, 968117788, -123449607, 1284210865, 435012392,
			-2017506339, -911064859, -370259173, 1132637927, 1398500161, -205601318, };
	
	//front pointer (java doesn't use pointers but I kept the name for reference)
	//orginally fptr = &randtbl[SEP_3 + 1] where SEP_3 was to 3
	private int fptr = 4;
	//rear pointer
	private int rptr = 1;
	//end pointer, orginally &randtbl[sizeof (randtbl) / sizeof (randtbl[0])]
	//shortened it to randtbl.length
	private int end_ptr = randtbl.length;
	/**
	 * Generate a 32 bit pseudo random number
	 * @return The next pseudo random number.
	 */
	public int rand() {
		int val = randtbl[fptr] += randtbl[rptr];
		/* Chucking least random bit. */
		int result = (val >> 1) & 0x7fffffff;
		++fptr;
		if (fptr >= end_ptr) {//move front pointer back to start 
			fptr = 1;
			++rptr;
		} else {
			++rptr;
			if (rptr >= end_ptr)//move end pointer back to start
				rptr = 1;
		}
		
		return result;
	}
}
