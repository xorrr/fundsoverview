package org.xorrr.financegrabber.view;

import java.util.List;

import org.xorrr.financegrabber.model.FundProduct;
import org.xorrr.financegrabber.presenter.DashboardViewHandler;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;


public interface DashboardView extends View {

    public void init();

    public void setHandler(DashboardViewHandler handler);

    public Button getAddFundBtn();

    public void displayFunds(List<FundProduct> funds);

    public Table getFundTable();
}
