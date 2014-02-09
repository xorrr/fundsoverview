package org.xorrr.fundsoverview.di;

import org.xorrr.fundsoverview.db.MongoFundsDatastore;
import org.xorrr.fundsoverview.model.FundsDatastore;
import org.xorrr.fundsoverview.retrieval.FidelityFundDocumentAccessor;
import org.xorrr.fundsoverview.retrieval.FundDocumentAccessor;

import com.google.inject.AbstractModule;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(FundDocumentAccessor.class).to(FidelityFundDocumentAccessor.class);

        bind(FundsDatastore.class).to(MongoFundsDatastore.class);
    }
}
