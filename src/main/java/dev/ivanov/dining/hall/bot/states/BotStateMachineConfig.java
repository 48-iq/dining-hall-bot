package dev.ivanov.dining.hall.bot.states;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static dev.ivanov.dining.hall.bot.states.BotStates.*;
import static dev.ivanov.dining.hall.bot.states.BotEvents.*;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class BotStateMachineConfig extends EnumStateMachineConfigurerAdapter<BotStates, BotEvents> {
  @Override
  public void configure(final StateMachineStateConfigurer<BotStates, BotEvents> states) throws Exception {
    states
      .withStates()
      .initial(START)
      .end(END)
      .states(EnumSet.allOf(BotStates.class));
  }

  @Override
  public void configure(final StateMachineConfigurationConfigurer<BotStates, BotEvents> config) throws Exception {
    config
      .withConfiguration()
      .autoStartup(true);
  }

  @Override
  public void configure(final StateMachineTransitionConfigurer<BotStates, BotEvents> transitions) throws Exception {
    transitions
      .withExternal()
      .source(START).target(MAIN_MENU).event(TO_MAIN_MENU).and()
      .withExternal()
      .source(MAIN_MENU).target(ADMIN_PANEL).event(TO_ADMIN_PANEL).and()
      .withExternal()
      .source(ADMIN_PANEL).target(MAIN_MENU).event(TO_MAIN_MENU).and()
      .withExternal()
      .source(MAIN_MENU).target(USER_WEEK_MENU).event(TO_USER_WEEK_MENU).and()
      .withExternal()
      .source(USER_WEEK_MENU).target(MAIN_MENU).event(TO_MAIN_MENU).and()
      .withExternal()
      .source(MAIN_MENU).target(USER_REVIEWS).event(TO_USER_REVIEWS).and()
      .withExternal()
      .source(USER_REVIEWS).target(MAIN_MENU).event(TO_MAIN_MENU).and()
      .withExternal()
      .source(USER_REVIEWS).target(USER_CREATE_REVIEW).event(TO_USER_CREATE_REVIEW).and()
      .withExternal()
      .source(USER_CREATE_REVIEW).target(USER_REVIEWS).event(TO_USER_REVIEWS).and()
      .withExternal()
      .source(USER_CREATE_REVIEW).target(MAIN_MENU).event(TO_MAIN_MENU);
  }
}
