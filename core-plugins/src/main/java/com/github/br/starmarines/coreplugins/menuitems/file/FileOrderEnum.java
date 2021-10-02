package com.github.br.starmarines.coreplugins.menuitems.file;

/**
 * идем через 1000, чтобы дать возможность плагинописателям красиво встроиться
 */
public enum FileOrderEnum {

    NEW_LOCAL_GAME(0),



    CLOSE(Integer.MAX_VALUE),
    ;


    private final int order;

    FileOrderEnum(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
