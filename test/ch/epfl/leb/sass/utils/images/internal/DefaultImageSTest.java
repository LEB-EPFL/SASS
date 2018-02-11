/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.utils.images.internal;

import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 * Test suite for DefaultImageS.
 * 
 * @author Kyle M. Douglass
 */
public class DefaultImageSTest {
    
    private final int WIDTH = 2;
    private final int HEIGHT = 2;
    private final int BITDEPTH = 16;
    private final String TITLE = "SASS Image Dataset";
    
    DefaultImageS instance = null;
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    @Before
    public void setUp() {
        instance = new DefaultImageS(WIDTH, HEIGHT);
    }
    
    /**
     * Test of getBitDepth method, of class DefaultImageS.
     */
    @Test
    public void testGetBitDepth() throws ImageShapeException {
        System.out.println("getBitDepth");
        int expResult = BITDEPTH;
        int result = instance.getBitDepth();
        assertEquals(expResult, result);
        
        instance.addImage(new int[HEIGHT][WIDTH]);
        result = instance.getBitDepth();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTitle method, of class DefaultImageS.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        String expResult = TITLE;
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTitle method, of class DefaultImageS.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        String title = "new title";
        instance.setTitle(title);
        assertEquals(title, instance.getTitle());
    }

    /**
     * Test of serializeToArray method, of class DefaultImageS.
     */
    @Test
    public void testSerializeToArray()  throws ImageShapeException {
        System.out.println("serializeToArray");
        
        // This only verifies that the method runs, not that the implementation
        // correctly serializes the data.
        instance.addImage(new short [2][2]);
        byte[] expResult = instance.serializeToArray();

    }

    /**
     * Test of serializeToBuffer method, of class DefaultImageS.
     */
    @Test
    public void testSerializeToBuffer() throws ImageShapeException {
        System.out.println("serializeToBuffer");
        
        // This isn't so much a test that the image is correctly serialized,
        // but more of a check that SerializeToBuffer is working without
        // exception.
        instance.addImage(new short [2][2]);
        ByteBuffer expResult = ByteBuffer.wrap(instance.serializeToArray());
        ByteBuffer result = instance.serializeToBuffer();
        assertEquals(expResult, result);
    }

    /**
     * Test of addImage method, of class DefaultImageS.
     */
    @Test
    public void testAddImage_shortArrArr() throws ImageShapeException {
        System.out.println("addImage");
        
        // The current DefaultImageS implementation uses column-row order.
        // I'd like to change this back to row-order in the future if possible.
        short[][] image = {{1, 3}, {2, 4}};
        instance.addImage(image);
        
        short[] expResult = {1, 2, 3, 4};
        short[] result = instance.getPixelData(0);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of addImage method, of class DefaultImageS.
     */
    @Test(expected = ImageShapeException.class)
    public void testAddImage_shortArrArr_wrongSize() throws ImageShapeException {
        System.out.println("addImage_wrongSize");
        
        // The current DefaultImageS implementation uses column-row order.
        // I'd like to change this back to row-order in the future if possible.
        short[][] image = {{1, 3, 7}, {2, 4, 8}};
        instance.addImage(image);
    }
    
    /**
     * Test of addImage method, of class DefaultImageS.
     */
    @Test(expected = ImageShapeException.class)
    public void testAddImage_intArrArr_wrongSize() throws ImageShapeException {
        System.out.println("addImage_wrongSize");
        
        // The current DefaultImageS implementation uses column-row order.
        // I'd like to change this back to row-order in the future if possible.
        int[][] image = {{1, 3, 7}, {2, 4, 8}};
        instance.addImage(image);
    }
    
    /**
     * Test of addImage method, of class DefaultImageS.
     */
    @Test(expected = ImageShapeException.class)
    public void testAddImage_floatArrArr_wrongSize() throws ImageShapeException {
        System.out.println("addImage_wrongSize");
        
        // The current DefaultImageS implementation uses column-row order.
        // I'd like to change this back to row-order in the future if possible.
        float[][] image = {{1, 3, 7}, {2, 4, 8}};
        instance.addImage(image);
    }

    /**
     * Test of addImage method, of class DefaultImageS.
     */
    @Test
    public void testAddImage_intArrArr() throws ImageShapeException {
        System.out.println("addImage");
        
        int[][] image = {{2, 6}, {4, 8}};
        instance.addImage(image);
        
        short[] expResult = {2, 4, 6, 8};
        short[] result = instance.getPixelData(0);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of addImage method, of class DefaultImageS.
     */
    @Test
    public void testAddImage_floatArrArr() throws ImageShapeException {
        System.out.println("addImage");
        
        float[][] image = {{2, 7}, {3, 8}};
        instance.addImage(image);
        
        short[] expResult = {2, 3, 7, 8};
        short[] result = instance.getPixelData(0);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of concatenate method, of class DefaultImageS.
     */
    @Test
    public void testConcatenate() throws ImageShapeException {
        System.out.println("concatenate");
        instance.addImage(new short[][]{{1, 3}, {2, 4}});
        
        ImageS newImage = new DefaultImageS(WIDTH, HEIGHT);
        newImage.addImage(new short[][]{{2, 6}, {4, 8}});
        instance.concatenate(newImage);
        
        assertArrayEquals(new short[]{1, 2, 3, 4}, instance.getPixelData(0));
        assertArrayEquals(new short[]{2, 4, 6, 8}, instance.getPixelData(1));
    }
    
    /**
     * Test of concatenate method, of class DefaultImageS.
     */
    @Test(expected = ImageShapeException.class)
    public void testConcatenate_wrongSize() throws ImageShapeException {
        System.out.println("concatenate");
        instance.addImage(new short[][]{{1, 3}, {2, 4}});
        
        ImageS newImage = new DefaultImageS(3, 2);
        newImage.addImage(new short[][]{{1, 2, 6}, {1, 4, 8}});
        instance.concatenate(newImage);
    }

    /**
     * Test of getPixelData method, of class DefaultImageS.
     */
    @Test
    public void testGetPixelData() throws ImageShapeException {
        System.out.println("getPixelData");
        
        instance.addImage(new short[][]{{1, 3}, {2, 4}});
                
                
        short[] expResult = {1, 2, 3, 4};
        short[] result = instance.getPixelData(0);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getSlice method, of class DefaultImageS.
     */
    @Test
    public void testGetSlice() throws ImageShapeException {
        System.out.println("getSlice");

        instance.addImage( new short[][]{{1, 3}, {2, 4}} );
        instance.addImage( new short[][]{{2, 6}, {4, 8}} );

        int expResult = 1;
        instance.setSlice(1);
        assertEquals(expResult, instance.getSlice());
    }
    
    /**
     * Test of setSlice method, of class DefaultImageS.
     */
    @Test
    public void testSetSlice()  throws ImageShapeException {
        System.out.println("setSlice");
        testGetSlice();
    }

    /**
     * Test of saveAsTiffStack method, of class DefaultImageS.
     */
    @Test
    public void testSaveAsTiffStack() throws IOException, ImageShapeException {
        System.out.println("saveAsTiffStack");
        File file = tempDir.newFile("testFile.tif");

        // This only tests that the method runs without error, not that it's
        // saved correctly.
        instance.addImage(new short[][]{{1, 3}, {2, 4}});
        instance.saveAsTiffStack(file);
    }
    
    /**
     * Test of saveAsTiffStack method, of class DefaultImageS.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsTiffStackEmpty() throws IOException {
        System.out.println("saveAsTiffStackEmpty");
        File file = tempDir.newFile("testFile.txt");

        // No data in the dataset yet
        instance.saveAsTiffStack(file);
        
    }

    /**
     * Test of getWidth method, of class DefaultImageS.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        int expResult = WIDTH;
        int result = instance.getWidth();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHeight method, of class DefaultImageS.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        int expResult = HEIGHT;
        int result = instance.getHeight();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSize method, of class DefaultImageS.
     */
    @Test
    public void testGetSize() throws ImageShapeException {
        System.out.println("getSize");
        int expResult = 0;
        int result = instance.getSize();
        assertEquals(expResult, result);
        
        instance.addImage( new short[][]{{1, 3}, {2, 4}} );
        expResult = 1;
        result = instance.getSize();
        assertEquals(expResult, result);
    }
    
}
