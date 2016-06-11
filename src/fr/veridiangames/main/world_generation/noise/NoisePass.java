package fr.veridiangames.main.world_generation.noise;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class NoisePass
{
	private Noise noise;
	private long  seed;
	private int octave;
	private float amplitude;
	private float height;
	private float inverse;
	
	public NoisePass(long seed, int octave, float amplitude, float height, float inverse)
	{
		this.seed = seed;
		this.octave = octave;
		this.amplitude = amplitude;
		this.height = height;
		this.inverse = inverse;
		
		generateNoisePass();
	}
	
	private void generateNoisePass()
	{
		this.noise = new Noise(seed, octave, amplitude);
	}
	
	public float getNoisePass(float x, float y)
	{
		float n = this.noise.getNoise(x, y, height, inverse);
		return n;
	}
}
