package fr.veridiangames.main.world_generation.noise;

import fr.veridiangames.engine.utils.Color4f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoiseGeneration
{
	private long 			seed;
	private int				size;
	private List<NoisePass>	noisePasses;
	private float[][] 		noise;
	private Random 			rand;
	
	private int height;
	private boolean usingHeight;
	private float[][] heightData;

	public NoiseGeneration(long seed, int size)
	{
		this.seed = seed;
		this.noisePasses = new ArrayList<>();
		this.size = size;
		this.noise = new float[size][size];
		this.rand = new Random(seed);

		addNoisePasses(seed);
	}

	private void addNoisePasses(long seed)
	{
		add(new NoisePass(seed, 50, 5f, 0, 1));
	}

	public void add(NoisePass pass)
	{
		noisePasses.add(pass);
	}

	public void calcFinalNoise()
	{
		for (int xx = 0; xx < size; xx++)
		{
			for (int yy = 0; yy < size; yy++)
			{
				float n = -10000;
				for (NoisePass p : noisePasses)
				{
					float nn = p.getNoisePass(xx, yy);
					if (nn > n) n = nn;
				}
				noise[xx][yy] = n;
			}
		}
	}

	public float getNoise(int x, int y)
	{
		if (usingHeight)
			return height;
		
		return noise[x][y];
	}

	public float getExactNoise(float x, float y)
	{
		float n = -10000;
		for (NoisePass p : noisePasses)
		{
			float nn = p.getNoisePass(x, y);
			if (nn > n) n = nn;
		}
		return n;
	}
	


	public long getSeed()
	{
		return seed;
	}
}
