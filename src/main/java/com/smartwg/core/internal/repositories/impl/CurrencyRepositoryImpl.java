package com.smartwg.core.internal.repositories.impl;

import javax.inject.Named;

import com.smartwg.core.internal.domain.entities.Currency;
import com.smartwg.core.internal.repositories.CurrencyRepository;

@Named
public class CurrencyRepositoryImpl extends GenericRepositoryImpl<Currency> implements
    CurrencyRepository {

}
