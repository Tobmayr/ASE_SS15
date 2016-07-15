package com.smartwg.core.internal.services.pipeline;

/**
 * 
 * This interface represents a specific stage of Pipeline in which the concret processing happens.
 * Stages are fully modular and can be linked together arbitrary in a class which implements the
 * Pipeline-Interface
 * 
 * @author Tobias Ortmayr(to)
 * @param <E> DTO object on which Stage operations are performed
 * 
 */
public interface Stage<E extends PipelineDTO> {
  /**
   * Processes the passed dto accordingly and stores results/changes in it
   * 
   * @param dto
   * @throws NullPointerException if the passed parameter is null
   */
  void process(E dto);
}
