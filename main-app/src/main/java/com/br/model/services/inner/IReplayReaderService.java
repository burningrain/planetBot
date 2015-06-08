package com.br.model.services.inner;

import java.util.List;

public interface IReplayReaderService {

    List<String> getResponsesList(String filePath) throws Exception;
    List<String> getActionsList(String filePath) throws Exception;

}
