package dmescher.soapyhearts.common;

public enum BasicGameStatus {
  RUNNING_IDLE, WAITING_JOIN, START_GAME, GAME_STARTED, WAITING_GROUP, WAITING_TURN, 
  GAME_OVER, GAME_FAULT, INVALID_GAME
}

// RUNNING_IDLE:  Game is in a suspended state, waiting for a wakeup call
// WAITING_JOIN:  Game is waiting for one or more players to join before starting
//  Advanced Status:  Returns an int.  Number of players needed before game starts
// START_GAME:     Game is full, awaiting a request to start the game.
// GAME_STARTED:   Game is now going, starting first round
// WAITING_GROUP: Game is waiting on all players to complete an action
//  Advanced Status:  Returns an int.  LS Bit is player 1, MSB is MaxPlayer.  Bit value of 
//                    1 for each player the game is waiting on
// WAITING_TURN:  Game is waiting on a player to complete their turn.
//  Advanced Status:  Returns an int.  Player ID that the game is waiting on.
// GAME_OVER:  Game has completed successfully
//  Advanced Status:  Returns an int.  Player ID of winner.
// GAME_FAULT:  Game has encountered a fatal fault, game is over and not scorable.
//  Advanced Status:  Further details, game specifc.
// INVALID_GAME:  A bad game ID has been returned, but I don't want to throw an
//   exception.
