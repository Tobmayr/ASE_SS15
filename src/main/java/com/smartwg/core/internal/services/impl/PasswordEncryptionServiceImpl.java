package com.smartwg.core.internal.services.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Named;

import com.smartwg.core.internal.services.PasswordEncryptionService;

/**
 * 
 * @author ozdesimsek
 *
 */
@Named
public class PasswordEncryptionServiceImpl implements PasswordEncryptionService {

  @Override
  public boolean authenticate(String writtenPassword, byte[] encryptedPassword, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    byte[] encryptedWrittenPwd = getEncryptedPassword(writtenPassword, salt);


    return Arrays.equals(encryptedPassword, encryptedWrittenPwd);

  }

  @Override
  public byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException,
      InvalidKeySpecException {

    String alg = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 160;
    int iteration = 20000;

    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iteration, derivedKeyLength);

    SecretKeyFactory factory = SecretKeyFactory.getInstance(alg);

    return factory.generateSecret(spec).getEncoded();
  }

  @Override
  public byte[] generateSalt() throws NoSuchAlgorithmException {

    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    return salt;
  }

}
