package com.smartwg.core.internal.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 
 * @author ozdesimsek
 *
 */
public interface PasswordEncryptionService {

  /**
   * Checks if the written password for user equals to the encrypted password.
   * 
   * @param writtenPassword
   * @param encryptedPassword
   * @param salt
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  boolean authenticate(String writtenPassword, byte[] encryptedPassword, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException;

  /**
   * Encrypts the password with given salt using the PBKDFWithHmacSHA1 algorithm.
   * 
   * @param password
   * @param salt
   * @return encrypted password
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException,
      InvalidKeySpecException;

  /**
   * Generates the salt
   * 
   * @return salt
   * @throws NoSuchAlgorithmException
   */
  byte[] generateSalt() throws NoSuchAlgorithmException;

}
