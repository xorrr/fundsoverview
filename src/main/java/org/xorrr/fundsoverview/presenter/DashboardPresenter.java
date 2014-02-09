package org.xorrr.fundsoverview.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xorrr.fundsoverview.di.Module;
import org.xorrr.fundsoverview.eventbus.EventBus;
import org.xorrr.fundsoverview.eventbus.EventType;
import org.xorrr.fundsoverview.eventbus.events.FundAlreadyAddedHandler;
import org.xorrr.fundsoverview.eventbus.events.InvalidIsinEventHandler;
import org.xorrr.fundsoverview.model.Fund;
import org.xorrr.fundsoverview.model.ModelFacade;
import org.xorrr.fundsoverview.retrieval.InvalidIsinException;
import org.xorrr.fundsoverview.view.DashboardView;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DashboardPresenter implements DashboardViewHandler {

    DashboardView view;
    ModelFacade model;
    EventBus bus;
    private Injector injector;

    @Inject
    public DashboardPresenter(DashboardView view, ModelFacade model,
            EventBus bus) {
        this.view = view;
        this.model = model;
        this.bus = bus;

        this.injector = Guice.createInjector(new Module());
        bus.addHandler(EventType.FUND_ALREADY_ADDED,
                injector.getInstance(FundAlreadyAddedHandler.class));
        bus.addHandler(EventType.INVALID_ISIN,
                injector.getInstance(InvalidIsinEventHandler.class));
    }

    @Override
    public void addFund(Fund fp) {
        try {
            model.getBasicFinancialProduct(fp.getIsin());
            if (!model.checkIfIsinAlreadyAdded(fp.getIsin()))
                model.addFund(fp);
            else
                bus.fireEvent(EventType.FUND_ALREADY_ADDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidIsinException e) {
            bus.fireEvent(EventType.INVALID_ISIN);
        }
    }

    @Override
    public void showFunds() {
        List<Fund> funds = model.getFunds();
        List<Fund> fundsWithInfos = new ArrayList<>();

        iterateSavedFunds(funds, fundsWithInfos);
        view.displayFunds(fundsWithInfos);
    }

    @Override
    public void removeFundTableItems() {
        view.getFundTable().removeAllItems();
    }

    @Override
    public void deleteFund(String isin) {
        model.deleteFund(isin);
    }

    @Override
    public DashboardView getView() {
        return this.view;
    }

    private void iterateSavedFunds(List<Fund> funds, List<Fund> fundsWithInfos) {
        for (Fund fund : funds) {
            addExtractedToFunds(fundsWithInfos, fund);
        }
    }

    private void addExtractedToFunds(List<Fund> fundsWithInfos, Fund fund) {
        try {
            Fund fp = model.getBasicFinancialProduct(fund.getIsin());
            fundsWithInfos.add(fp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidIsinException e) {
            e.printStackTrace();
        }
    }
}
