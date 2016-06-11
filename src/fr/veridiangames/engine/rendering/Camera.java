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

package fr.veridiangames.engine.rendering;

import fr.veridiangames.engine.maths.Mat4;
import fr.veridiangames.engine.maths.Quat;
import fr.veridiangames.engine.maths.Transform;
import fr.veridiangames.engine.maths.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Created by Marc on 26/05/2016.
 */
public class Camera
{
    private Transform transform;

    private Mat4 projectionMatrix;

    public Camera()
    {
        this(new Vec3());
    }

    public void update()
    {
        if (Mouse.isButtonDown(0))
            Mouse.setGrabbed(true);
        if (!Mouse.isGrabbed())
            return;
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            Mouse.setGrabbed(false);

        float mouseSpeed = 0.5f;
        float movementSpeed = 0.1f;

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            movementSpeed = 1;

        transform.rotate(Vec3.UP, Mouse.getDX() * mouseSpeed);
        transform.rotate(transform.getRight(), -Mouse.getDY() * mouseSpeed);

        if (Keyboard.isKeyDown(Keyboard.KEY_Z))
            transform.getLocalPosition().add(transform.getForward().copy().mul(movementSpeed));
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            transform.getLocalPosition().add(transform.getBack().copy().mul(movementSpeed));
        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
            transform.getLocalPosition().add(transform.getLeft().copy().mul(movementSpeed));
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            transform.getLocalPosition().add(transform.getRight().copy().mul(movementSpeed));
    }

    public Camera(Vec3 position)
    {
        this(position, new Quat());
    }

    public Camera(Vec3 position, Quat rot)
    {
        this(position, rot, new Vec3());
    }

    public Camera(Vec3 position, Quat rot, Vec3 size)
    {
        this.transform = new Transform(position, rot, size);
    }

    public Mat4 getProjection()
    {
        Mat4 translationMatrix = Mat4.translate(transform.getPosition().copy().negate());
        Mat4 rotationMatrix = Mat4.rotate(transform.getForward(), transform.getUp());

        return projectionMatrix.mul(rotationMatrix.mul(translationMatrix));
    }

    public void setProjection(Mat4 projectionMatrix)
    {
        this.projectionMatrix = projectionMatrix; //Mat4.orthographic(-20, 20, 20, -20, -30.0f, 30.0f); //(fov, width / height, near, far);
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void setTransform(Transform transform)
    {
        this.transform = transform;
    }
}
