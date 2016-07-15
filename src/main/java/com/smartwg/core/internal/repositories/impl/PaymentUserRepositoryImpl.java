package com.smartwg.core.internal.repositories.impl;

import javax.inject.Named;

import com.smartwg.core.internal.domain.entities.PaymentUser;
import com.smartwg.core.internal.repositories.PaymentUserRepository;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named
public class PaymentUserRepositoryImpl extends GenericRepositoryImpl<PaymentUser> implements
    PaymentUserRepository {

}
