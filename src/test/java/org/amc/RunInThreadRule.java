package org.amc;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


/**
 * https://gist.github.com/fappel/65982e5ea7a6b2fde5a3
 *
 */
public class RunInThreadRule implements TestRule {

  @Override
  public Statement apply( Statement base, Description description ) {
    Statement result = base;
    RunInThread annotation = description.getAnnotation( RunInThread.class );
    if( annotation != null ) {
      result = new RunInThreadStatement( base );
    }
    return result;
  }
}
