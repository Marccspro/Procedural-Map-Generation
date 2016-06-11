/*
 *  Copyright (C) 2016 Marccspro
 *
 *     This file is part of "Procedural Map Generation".
 *
 *         "Procedural Map Generation" is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         "Procedural Map Generation" is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with "Procedural Map Generation".  If not, see http://www.gnu.org/licenses/.
 */

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
