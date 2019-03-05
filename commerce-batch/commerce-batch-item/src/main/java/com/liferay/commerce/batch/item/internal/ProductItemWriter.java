package com.liferay.commerce.batch.item.internal;

import com.liferay.commerce.batch.engine.api.item.ItemWriter;
import com.liferay.commerce.batch.item.internal.model.ProductDTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;

@Component(
    property = {"type=Product", "version=v1.0", "operation=CREATE"},
    service = ItemWriter.class,
    scope = ServiceScope.PROTOTYPE)
public class ProductItemWriter implements ItemWriter<ProductDTO> {

    @Override
    public void write(List<? extends ProductDTO> items) throws Exception {
        System.out.println(items);
    }

}
