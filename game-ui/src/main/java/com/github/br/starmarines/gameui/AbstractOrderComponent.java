package com.github.br.starmarines.gameui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import com.github.br.starmarines.ui.api.IUiComponent;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * https://groups.google.com/forum/#!topic/wisdom-discuss/hoaxFBs84Dw - суть
 * проблемы
 * http://felix.apache.org/ipojo/api/1.12.1/org/apache/felix/ipojo/manipulator
 * /metadata/annotation/visitor/bind/MethodBindVisitor.html
 * <p>
 * https://groups.google.com/forum/#!topic/wisdom-discuss/hoaxFBs84Dw
 * https://groups.google.com/forum/#!msg/wisdom-discuss/5-rHPjhNdOA/9MwMyokGN_UJ
 * хотели добавить возможность искать аннотации в суперклассах в 15 году, но
 * версия ipojo ни в 15, ни в 16 году так и не вышла
 *
 * @param <FXPARENT> <b>куда вкладываем</b> javafx-элемент, к которому цепляются элементы из
 *                   IUI.getNode()
 * @param <FXCHILD>  <b>что вкладываем</b>  тип javafx-элемента, который указан в
 *                   наследнике {@link IUiComponent}
 * @param <ISERVICE> <b>откуда берем то, что вкладываем</b> {@link IUiComponent} и его
 *                   производные
 * @author burning rain
 */
public abstract class AbstractOrderComponent<FXPARENT, FXCHILD, ISERVICE extends IUiOrderComponent<FXCHILD>>
        implements IUiComponent<FXPARENT> {

    private volatile boolean isInit = false;
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    private FXPARENT fxParent;
    private ObservableList<FXCHILD> fxChildren;
    private ConcurrentMap<String, PairFxContainer<FXPARENT, FXCHILD, ISERVICE>> fxPairMap = new ConcurrentHashMap<>();

    //FIXME нельзя повесить @Validate. Не поддерживается Ipojo :(
    public void init(FXPARENT component, ObservableList<FXCHILD> children) {
        this.fxParent = component;
        this.fxChildren = children;

        this.isInit = true;
    }

    public CompletableFuture<PairFxContainer<FXPARENT, FXCHILD, ISERVICE>> bind(ISERVICE uiComponentImpl) {
        return CompletableFuture.supplyAsync(() -> {
            while (!isInit && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String key = getConstUID(uiComponentImpl);
            if (fxPairMap.containsKey(key)) {
                throw new RuntimeException(
                        String.format("Элемент %s '%s' уже существует",
                                uiComponentImpl.getClass().getCanonicalName(),
                                uiComponentImpl.toString())
                );
            }

            PairFxContainer<FXPARENT, FXCHILD, ISERVICE> pairFxContainer = new PairFxContainer<>(
                    uiComponentImpl, fxParent);
            Platform.runLater(() ->

            {
                addToUI(new ArrayList<PairFxContainer<FXPARENT, FXCHILD, ISERVICE>>(
                        fxPairMap.values()), fxChildren);
            });
            fxPairMap.put(key, pairFxContainer);
            return pairFxContainer;
        }, executor);
    }

    protected void addToUI(
            List<PairFxContainer<FXPARENT, FXCHILD, ISERVICE>> allUiPairs,
            ObservableList<FXCHILD> children) {
        Collections.sort(allUiPairs);
        children.clear();
        List<FXCHILD> elements = allUiPairs.stream().map(a -> a.getFxChild())
                .collect(Collectors.toList());
        children.addAll(elements);
    }

    public PairFxContainer<FXPARENT, FXCHILD, ISERVICE> unbind(ISERVICE uiComponentImpl) {
        String key = getConstUID(uiComponentImpl);
        PairFxContainer<FXPARENT, FXCHILD, ISERVICE> fxPair = fxPairMap.get(key);
        if (fxPair == null) {
            throw new RuntimeException(String.format(
                    "Компонент интерфейса '%s' не был зарегистрирован!",
                    uiComponentImpl.getClass().getName()));
        }

        Platform.runLater(() -> {
            deleteFromUI(fxPair, fxChildren);
        });
        fxPairMap.remove(key);
        return fxPair;
    }

    protected void deleteFromUI(
            PairFxContainer<FXPARENT, FXCHILD, ISERVICE> fxPair,
            ObservableList<FXCHILD> children) {
        Optional<FXCHILD> menuElement = children.stream()
                .filter(m -> m.equals(fxPair.getFxChild())).findAny();
        if (menuElement.isPresent()) {
            children.remove(menuElement.get());
        } else {
            throw new RuntimeException(String.format(
                    "Элемент интерфейса '%s' (%s) не найден!",
                    getConstUID(fxPair.getService()), fxPair.getService().toString()));
        }
    }

    @Override
    public FXPARENT getNode() {
        return this.fxParent;
    }

    protected abstract String getConstUID(ISERVICE uiComponentImpl);

}
