package com.github.wu.common.spi;

/**
 * @author wangyongxu
 */
public class DefaultNameGenerator implements NameGenerator {
    @Override
    public String generate(Class<?> cls) {
        String simpleName = cls.getSimpleName();
        char[] ch = simpleName.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return String.valueOf(ch);
    }
}
