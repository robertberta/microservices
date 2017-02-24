package com.visma.approval;

import com.visma.approval.view.FieldDescriptorStore;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by robert on 10.02.2017.
 */
public class FieldDescriptorStoreTests {
    FieldDescriptorStore store = new FieldDescriptorStore();

    @Test
    public void testMandatoryAndOptionalOverlap() throws ParseException {
        assertTrue(store.getWorkflowDocumentNames().size()>0);
        for (String name:store.getWorkflowDocumentNames()){
            Collection overlapping = CollectionUtils.intersection(store.getMandatoryFields(name), store.getOptionalFields(name));
            assertEquals(0,overlapping.size());
        }
    }
}
