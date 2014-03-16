package de.fhg.iais.roberta.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;

public class BlocklyTest {
    @Test
    public void test1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Block.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(BlocklyTest.class.getResourceAsStream("/blockly-test-1.xml"));
        Block block = (Block) jaxbUnmarshaller.unmarshal(src);

        Value value = block.getValue().get(0);
        Assert.assertEquals("TIMES", value.getName());
    }

    @Test
    public void test2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Block.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(BlocklyTest.class.getResourceAsStream("/blockly-test-2.xml"));
        Block block = (Block) jaxbUnmarshaller.unmarshal(src);

        Field field = block.getValue().get(1).getBlock().getField().get(0);
        Assert.assertEquals("OP", field.getName());
        Assert.assertEquals("ADD", field.getValue());
    }

}
