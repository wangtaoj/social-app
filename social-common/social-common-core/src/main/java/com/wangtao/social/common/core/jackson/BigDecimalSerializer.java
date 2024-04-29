package com.wangtao.social.common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * BigDecimal序列化器
 *
 * @author wangtao
 * Created at 2023-09-26
 */
public class BigDecimalSerializer extends StdSerializer<BigDecimal> {

    @Serial
    private static final long serialVersionUID = -82683766308966384L;

    public BigDecimalSerializer() {
        super(BigDecimal.class);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.nonNull(value)) {
            gen.writeString(value.stripTrailingZeros().toPlainString());
        } else {
            gen.writeNull();
        }
    }
}
