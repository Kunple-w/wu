package com.github.wu.core.rpc.loadbalancestrategy;

import com.github.wu.common.URL;
import com.github.wu.common.spi.SPIAlias;
import com.github.wu.core.rpc.LoadBalance;
import com.github.wu.core.rpc.Reference;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * @author qiankewei
 */
@SPIAlias(alias = "random")
public class RandomLoadBalance<T> implements LoadBalance<T> {
    @Override
    public Reference<T> select(List<Reference<T>> references, URL url) {
        int i = RandomUtils.nextInt(0, references.size());
        return references.get(i);
    }
}
