package com.liferay.commerce.batch.item.internal;

import com.liferay.commerce.batch.engine.api.item.BaseJSONItemReader;
import com.liferay.commerce.batch.engine.api.item.ItemReader;
import com.liferay.commerce.batch.item.internal.model.ProductDTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(
    property = {"type=Product", "version=v1.0", "operation=CREATE"},
    service = ItemReader.class, scope = ServiceScope.PROTOTYPE)
public class ProductItemReader extends BaseJSONItemReader<ProductDTO> {

    @Override
    public Class<ProductDTO> getType() {
        return ProductDTO.class;
    }

}
