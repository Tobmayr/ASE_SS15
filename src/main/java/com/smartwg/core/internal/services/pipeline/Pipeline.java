package com.smartwg.core.internal.services.pipeline;



/**
 * Provides a Method for passing a DTO object through various stages of a generic pipeline. Although
 * the method itself returns void the passed dto object will be modified and adapted during the
 * pipeline stages and after the whole pipeline has been finished the final result will be stored in
 * it. The implementing class has to provide a set of object which are implementing the
 * Stage-Interface. These stages will be processed in a sequentiall way (e.g iterating over a List
 * of stage objects)
 * 
 * @author Tobias Ortmayr (to)
 * 
 * @param <E> dto object on which pipeline operations are performed
 * 
 */
public interface Pipeline<E extends PipelineDTO> {

  /**
   * Starts the pipeline and passes the dto object sequential between all stages which process some
   * operations and store the result in the dto.
   * 
   * @param dto object on which pipeline operations are performed
   * @throws NullPointerException if the passed parameter is null
   */
  void doPipeline(E dto);
}
