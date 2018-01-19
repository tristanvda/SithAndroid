package com.grietenenknapen.sithandroid.maingame;

import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCasePairId;
import com.grietenenknapen.sithandroid.game.usecase.type.UseCaseYesNo;
import com.grietenenknapen.sithandroid.maingame.usecases.BobaFettUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.SithUseCase;
import com.grietenenknapen.sithandroid.maingame.usecases.UseCaseCard;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameSide;
import com.grietenenknapen.sithandroid.model.game.GameTeam;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(RobolectricTestRunner.class)
public class MainGameFlowManagerTest {

    private MainGame mainGame;
    private MainGameFlowManager mainGameFlowManager;
    private ActivePlayer bb8Player;
    private ActivePlayer bobaFettPlayer;
    private ActivePlayer hanSoloPlayer;
    private ActivePlayer kyloRenPlayer;
    private ActivePlayer mazKanataPlayer;
    private ActivePlayer sithPlayer;
    private ActivePlayer jediPlayer;

    @Mock
    private MainGameFlowCallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        final List<ActivePlayer> activePlayers = new ArrayList<>();

        bb8Player = ActivePlayer.newBuilder()
                .player_id(1L)
                .side(GameSide.JEDI)
                .team(GameTeam.JEDI)
                .sithCard(MainGameDefaults.getSithCardBB8())
                .name("bb8")
                .alive(true)
                .build();
        activePlayers.add(bb8Player);
        bobaFettPlayer = ActivePlayer.newBuilder()
                .player_id(2L)
                .side(GameSide.JEDI)
                .team(GameTeam.JEDI)
                .sithCard(MainGameDefaults.getSithCardBobaFett())
                .name("boba fett")
                .alive(true)
                .build();
        activePlayers.add(bobaFettPlayer);
        hanSoloPlayer = ActivePlayer.newBuilder()
                .player_id(3L)
                .side(GameSide.JEDI)
                .team(GameTeam.JEDI)
                .sithCard(MainGameDefaults.getSithCardHanSolo())
                .name("han solo")
                .alive(true)
                .build();
        activePlayers.add(hanSoloPlayer);
        kyloRenPlayer = ActivePlayer.newBuilder()
                .player_id(4L)
                .side(GameSide.JEDI)
                .team(GameTeam.JEDI)
                .sithCard(MainGameDefaults.getSithCardKyloRen())
                .name("kylo ren")
                .alive(true)
                .build();
        activePlayers.add(kyloRenPlayer);
        mazKanataPlayer = ActivePlayer.newBuilder()
                .player_id(5L)
                .side(GameSide.JEDI)
                .team(GameTeam.JEDI)
                .sithCard(MainGameDefaults.getSithCardMazKanata())
                .name("maz kanata")
                .alive(true)
                .build();
        activePlayers.add(mazKanataPlayer);
        sithPlayer = ActivePlayer.newBuilder()
                .player_id(6L)
                .side(GameSide.SITH)
                .team(GameTeam.SITH)
                .sithCard(MainGameDefaults.getSithCardDartVader())
                .name("darth vader")
                .alive(true)
                .build();
        activePlayers.add(sithPlayer);
        jediPlayer = ActivePlayer.newBuilder()
                .player_id(7L)
                .side(GameSide.JEDI)
                .team(GameTeam.JEDI)
                .sithCard(MainGameDefaults.getSithCardLuke())
                .name("luke")
                .alive(true)
                .build();
        activePlayers.add(jediPlayer);


        final List<SithCard> sithCards = new ArrayList<>();
        sithCards.add(MainGameDefaults.getSithCardBB8());
        sithCards.add(MainGameDefaults.getSithCardBobaFett());
        sithCards.add(MainGameDefaults.getSithCardChewBacca());
        sithCards.add(MainGameDefaults.getSithCardDarthMaul());
        sithCards.add(MainGameDefaults.getSithCardDartVader());
        sithCards.add(MainGameDefaults.getSithCardFinn());
        sithCards.add(MainGameDefaults.getSithCardGeneralGrievous());
        sithCards.add(MainGameDefaults.getSithCardHanSolo());
        sithCards.add(MainGameDefaults.getSithCardKyloRen());
        sithCards.add(MainGameDefaults.getSithCardLuke());
        sithCards.add(MainGameDefaults.getSithCardMazKanata());
        sithCards.add(MainGameDefaults.getSithCardObiWanKenobi());
        sithCards.add(MainGameDefaults.getSithCardRey());
        sithCards.add(MainGameDefaults.getSithCardTheEmperor());

        mainGame = new MainGame(activePlayers);
        mainGameFlowManager = new MainGameFlowManager(mainGame, sithCards);

    }

    @Test
    public void mainGameFlow_SithWin_ASAP_Success() throws InterruptedException {

        Assert.assertEquals(mainGameFlowManager.isGameOver(), false);

        final ActivePlayer lover1 = bb8Player;
        final ActivePlayer lover2 = mazKanataPlayer;
        final AtomicInteger callbackCount = new AtomicInteger(8);

        mainGameFlowManager.attach(new MainGameFlowCallBackAdapter() {

            @Override
            public void requestUserPairPlayerSelection(List<ActivePlayer> players) {
                //link two jedi users and kill them in this round
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCasePairId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(),
                        new Pair<>(lover1.getPlayerId(), lover2.getPlayerId()));
                callbackCount.decrementAndGet();
            }

            @Override
            public void showDelay(long delay) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void requestYesNoAnswer(boolean disableYes, int titleResId) {
                //Boba fett is going to kill the last jedi player and use his rocket launcher
                if (mainGame.getCurrentRound() == 1) {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 2) {
                    //In the second round, the rocket is already used
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                }
            }

            @Override
            public void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId) {
                //To make the sith win ASAP, Boba fett is not going to use his med pack
                ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserPlayerSelection(List<ActivePlayer> activePlayers) {
                if (mainGameFlowManager.getActiveGameUseCase() instanceof SithUseCase) {
                    //The sith kill one of the lovers
                    if (mainGame.getCurrentRound() == 1) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), lover1.getPlayerId());
                        callbackCount.decrementAndGet();
                    } else if (mainGame.getCurrentRound() == 2) {
                        //Kill kylo ren in the last round
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), kyloRenPlayer.getPlayerId());
                    }
                } else if (mainGameFlowManager.getActiveGameUseCase() instanceof BobaFettUseCase) {
                    //Boba fett kills the last jedi player
                    ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), jediPlayer.getPlayerId());
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserCardSelection(List<SithCard> availableSithCards) {
                //To make the sith win ASAP, Han Solo is going to select a sith card
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCaseCard) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), MainGameDefaults.getSithCardDarthMaul());
                callbackCount.decrementAndGet();
            }

            @Override
            public void requestUserCardPeek(List<ActivePlayer> players, long delay) {
                //Maz kanata is going to look into a random card
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(1);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void speak(int soundResId, int stringResId) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void roundStatusChanged(boolean started, boolean gameOver) {
                //After the first round, only boba Fett and kylo renn are still alive on the good side, so the game is not yet over
                if (mainGame.getCurrentRound() == 1 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 2 && !started) {
                    Assert.assertTrue(gameOver);
                    Assert.assertEquals(mainGame.getWinningTeam(), GameTeam.SITH);
                    List<ActivePlayer> alivePlayers = mainGame.getAlivePlayers();
                    Assert.assertEquals(alivePlayers.size(), 2);
                    Assert.assertTrue(alivePlayers.contains(sithPlayer));
                    //THe alive list also contains Han Solo because he changed his card to a Sith card
                    Assert.assertTrue(alivePlayers.contains(hanSoloPlayer));
                    callbackCount.decrementAndGet();
                }
            }
        });

        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertEquals(hanSoloPlayer.getSithCard().getId(), MainGameDefaults.getSithCardDarthMaul().getId());
        Assert.assertEquals(mainGame.getLovers().first.getPlayerId(), lover1.getPlayerId());
        Assert.assertEquals(mainGame.getLovers().second.getPlayerId(), lover2.getPlayerId());
        Assert.assertFalse(mainGame.isGameOver());

        //Now Kill Boba Fett
        List<Player> killedPlayers = new ArrayList<>();
        killedPlayers.add(Player.newBuilder()._id(bobaFettPlayer.getPlayerId()).build());
        mainGame.killPlayers(killedPlayers);

        Assert.assertFalse(mainGame.isGameOver());

        callbackCount.set(1);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertTrue(mainGame.isGameOver());
    }

    @Test
    public void mainGameFlow_JediWin_ASAP_Success() throws InterruptedException {

        Assert.assertEquals(mainGameFlowManager.isGameOver(), false);

        final ActivePlayer lover1 = jediPlayer;
        final ActivePlayer lover2 = mazKanataPlayer;
        final AtomicInteger callbackCount = new AtomicInteger(8);

        mainGameFlowManager.attach(new MainGameFlowCallBackAdapter() {

            @Override
            public void requestUserPairPlayerSelection(List<ActivePlayer> players) {
                //link two jedi users
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCasePairId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), new Pair<>(lover1.getPlayerId(), lover2.getPlayerId()));
                callbackCount.decrementAndGet();
            }

            @Override
            public void showDelay(long delay) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void requestYesNoAnswer(boolean disableYes, int titleResId) {
                //Boba fett is going to kill the only Sith player to make the Jedi win ASAP
                ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                callbackCount.decrementAndGet();
            }

            @Override
            public void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId) {
                //Boba fett is going to rescue the killed jedi
                ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserPlayerSelection(List<ActivePlayer> activePlayers) {
                if (mainGameFlowManager.getActiveGameUseCase() instanceof SithUseCase) {
                    //The sith kill the jedi
                    ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), jediPlayer.getPlayerId());
                    callbackCount.decrementAndGet();
                } else if (mainGameFlowManager.getActiveGameUseCase() instanceof BobaFettUseCase) {
                    //Boba fett kills the Sith
                    ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), sithPlayer.getPlayerId());
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserCardSelection(List<SithCard> availableSithCards) {
                //To make the jedi win ASAP, Han Solo is going to select a Jedi card
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCaseCard) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), MainGameDefaults.getSithCardObiWanKenobi());
                callbackCount.decrementAndGet();
            }

            @Override
            public void requestUserCardPeek(List<ActivePlayer> players, long delay) {
                //Maz kanata is going to look into a random card
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(1);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void speak(int soundResId, int stringResId) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void roundStatusChanged(boolean started, boolean gameOver) {
                //After the first round, only boba Fett and kylo renn are still alive on the good side, so the game is not yet over
                if (mainGame.getCurrentRound() == 1 && !started) {
                    Assert.assertTrue(gameOver);
                    callbackCount.decrementAndGet();
                    Assert.assertEquals(mainGame.getWinningTeam(), GameTeam.JEDI);
                    List<ActivePlayer> alivePlayers = mainGame.getAlivePlayers();
                    Assert.assertEquals(alivePlayers.size(), 6);
                    Assert.assertTrue(alivePlayers.contains(hanSoloPlayer));
                    Assert.assertTrue(alivePlayers.contains(mazKanataPlayer));
                    Assert.assertTrue(alivePlayers.contains(jediPlayer));
                    Assert.assertTrue(alivePlayers.contains(bb8Player));
                    Assert.assertTrue(alivePlayers.contains(bobaFettPlayer));
                    Assert.assertTrue(alivePlayers.contains(kyloRenPlayer));
                }
            }
        });

        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertEquals(hanSoloPlayer.getSithCard().getId(), MainGameDefaults.getSithCardObiWanKenobi().getId());
        Assert.assertEquals(mainGame.getLovers().first.getPlayerId(), lover1.getPlayerId());
        Assert.assertEquals(mainGame.getLovers().second.getPlayerId(), lover2.getPlayerId());
        Assert.assertTrue(mainGame.isGameOver());
    }

    @Test
    public void mainGameFlow_LoversWin_Night3_Success() throws InterruptedException {

        Assert.assertEquals(mainGameFlowManager.isGameOver(), false);

        final ActivePlayer lover1 = sithPlayer;
        final ActivePlayer lover2 = mazKanataPlayer;
        final AtomicInteger callbackCount = new AtomicInteger(8);

        mainGameFlowManager.attach(new MainGameFlowCallBackAdapter() {

            @Override
            public void requestUserPairPlayerSelection(List<ActivePlayer> players) {
                //link two a sith and Jedi player, so they have to win together
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCasePairId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(),
                        new Pair<>(lover1.getPlayerId(), lover2.getPlayerId()));
                callbackCount.decrementAndGet();
            }

            @Override
            public void showDelay(long delay) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void requestYesNoAnswer(boolean disableYes, int titleResId) {
                //Boba fett is going to kill a jedi player the first round
                if (mainGame.getCurrentRound() == 1) {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                    callbackCount.decrementAndGet();
                } else {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                }
            }

            @Override
            public void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId) {
                //Boba fett is never going to rescue the killed player
                ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserPlayerSelection(List<ActivePlayer> activePlayers) {
                if (mainGameFlowManager.getActiveGameUseCase() instanceof SithUseCase) {
                    //The sith kill boba Fett
                    if (mainGame.getCurrentRound() == 1) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), bobaFettPlayer.getPlayerId());
                        callbackCount.decrementAndGet();
                    } else if (mainGame.getCurrentRound() == 2) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), hanSoloPlayer.getPlayerId());
                    } else if (mainGame.getCurrentRound() == 3) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), bb8Player.getPlayerId());
                    }
                } else if (mainGameFlowManager.getActiveGameUseCase() instanceof BobaFettUseCase) {
                    ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), jediPlayer.getPlayerId());
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserCardSelection(List<SithCard> availableSithCards) {
                //Han Solo is going to take a Sith card
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCaseCard) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), MainGameDefaults.getSithCardDarthMaul());
                callbackCount.decrementAndGet();
            }

            @Override
            public void requestUserCardPeek(List<ActivePlayer> players, long delay) {
                //Maz kanata is going to look into a random card)
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(1);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void speak(int soundResId, int stringResId) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void roundStatusChanged(boolean started, boolean gameOver) {
                //After the first round, only boba Fett and kylo renn are still alive on the good side, so the game is not yet over
                if (mainGame.getCurrentRound() == 1 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 2 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 3 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                }
            }
        });

        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertEquals(hanSoloPlayer.getSithCard().getId(), MainGameDefaults.getSithCardDarthMaul().getId());
        Assert.assertEquals(mainGame.getLovers().first.getPlayerId(), lover1.getPlayerId());
        Assert.assertEquals(mainGame.getLovers().second.getPlayerId(), lover2.getPlayerId());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertFalse(mainGame.isGameOver());

        //Nobody is going to day during the day

        callbackCount.set(1);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertFalse(mainGame.isGameOver());

        callbackCount.set(1);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        //Check if kylo renn converted to sith side
        Assert.assertEquals(kyloRenPlayer.getSide(), GameSide.SITH);
        Assert.assertFalse(mainGame.isGameOver());

        //Now Kill Kylo Renn
        List<Player> killedPlayers = new ArrayList<>();
        killedPlayers.add(Player.newBuilder()._id(kyloRenPlayer.getPlayerId()).build());
        mainGame.killPlayers(killedPlayers);

        //Now the game should be over
        Assert.assertEquals(mainGame.getWinningTeam(), GameTeam.LOVERS);
        List<ActivePlayer> alivePlayers = mainGame.getAlivePlayers();
        Assert.assertEquals(alivePlayers.size(), 2);
        Assert.assertTrue(alivePlayers.contains(lover1));
        Assert.assertTrue(alivePlayers.contains(lover2));
    }

    @Test
    public void mainGameFlow_LoversChangeSideAndWin_Night3_Success() throws InterruptedException {

        Assert.assertEquals(mainGameFlowManager.isGameOver(), false);

        final ActivePlayer lover1 = kyloRenPlayer;
        final ActivePlayer lover2 = mazKanataPlayer;
        final AtomicInteger callbackCount = new AtomicInteger(8);

        mainGameFlowManager.attach(new MainGameFlowCallBackAdapter() {

            @Override
            public void requestUserPairPlayerSelection(List<ActivePlayer> players) {
                //link two a sith and Jedi player, so they have to win together
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCasePairId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(),
                        new Pair<>(lover1.getPlayerId(), lover2.getPlayerId()));
                callbackCount.decrementAndGet();
            }

            @Override
            public void showDelay(long delay) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void requestYesNoAnswer(boolean disableYes, int titleResId) {
                //Boba fett is going to kill a jedi player the first round
                if (mainGame.getCurrentRound() == 1) {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                    callbackCount.decrementAndGet();
                } else {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                }
            }

            @Override
            public void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId) {
                //Boba fett is never going to rescue the killed player
                ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserPlayerSelection(List<ActivePlayer> activePlayers) {
                if (mainGameFlowManager.getActiveGameUseCase() instanceof SithUseCase) {
                    //The sith kill boba Fett
                    if (mainGame.getCurrentRound() == 1) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), bobaFettPlayer.getPlayerId());
                        callbackCount.decrementAndGet();
                    } else if (mainGame.getCurrentRound() == 2) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), hanSoloPlayer.getPlayerId());
                    } else if (mainGame.getCurrentRound() == 3) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), bb8Player.getPlayerId());
                    }
                } else if (mainGameFlowManager.getActiveGameUseCase() instanceof BobaFettUseCase) {
                    ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), jediPlayer.getPlayerId());
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserCardSelection(List<SithCard> availableSithCards) {
                //Han Solo is going to take a Sith card
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCaseCard) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), MainGameDefaults.getSithCardDarthMaul());
                callbackCount.decrementAndGet();
            }

            @Override
            public void requestUserCardPeek(List<ActivePlayer> players, long delay) {
                //Maz kanata is going to look into a random card)
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(1);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void speak(int soundResId, int stringResId) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void roundStatusChanged(boolean started, boolean gameOver) {
                //After the first round, only boba Fett and kylo renn are still alive on the good side, so the game is not yet over
                if (mainGame.getCurrentRound() == 1 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 2 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 3 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                }
            }
        });

        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertEquals(hanSoloPlayer.getSithCard().getId(), MainGameDefaults.getSithCardDarthMaul().getId());
        Assert.assertEquals(mainGame.getLovers().first.getPlayerId(), lover1.getPlayerId());
        Assert.assertEquals(mainGame.getLovers().second.getPlayerId(), lover2.getPlayerId());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertFalse(mainGame.isGameOver());

        //Nobody is going to day during the day

        callbackCount.set(1);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertFalse(mainGame.isGameOver());

        callbackCount.set(1);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        //Check if kylo renn converted to sith side
        Assert.assertEquals(kyloRenPlayer.getSide(), GameSide.SITH);
        Assert.assertFalse(mainGame.isGameOver());

        //Now Kill the last Sith (except for kylo ren) and let the lovers win
        List<Player> killedPlayers = new ArrayList<>();
        killedPlayers.add(Player.newBuilder()._id(sithPlayer.getPlayerId()).build());
        mainGame.killPlayers(killedPlayers);

        //Now the game should be over
        Assert.assertEquals(mainGame.getWinningTeam(), GameTeam.LOVERS);
        List<ActivePlayer> alivePlayers = mainGame.getAlivePlayers();
        Assert.assertEquals(alivePlayers.size(), 2);
        Assert.assertTrue(alivePlayers.contains(lover1));
        Assert.assertTrue(alivePlayers.contains(lover2));
    }

    @Test
    public void mainGameFlow_LoversWinWithJedi_Night3_Success() throws InterruptedException {

        Assert.assertEquals(mainGameFlowManager.isGameOver(), false);

        final ActivePlayer lover1 = bb8Player;
        final ActivePlayer lover2 = mazKanataPlayer;
        final AtomicInteger callbackCount = new AtomicInteger(8);

        mainGameFlowManager.attach(new MainGameFlowCallBackAdapter() {

            @Override
            public void requestUserPairPlayerSelection(List<ActivePlayer> players) {
                //link two a sith and Jedi player, so they have to win together
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCasePairId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(),
                        new Pair<>(lover1.getPlayerId(), lover2.getPlayerId()));
                callbackCount.decrementAndGet();
            }

            @Override
            public void showDelay(long delay) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void requestYesNoAnswer(boolean disableYes, int titleResId) {
                //Boba fett is going to kill a jedi player the first round
                if (mainGame.getCurrentRound() == 1) {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                    callbackCount.decrementAndGet();
                } else {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                }
            }

            @Override
            public void showPlayerYesNo(ActivePlayer activePlayer, boolean disableYes, int titleResId) {
                //Boba fett is never going to rescue the killed player
                if (mainGame.getCurrentRound() == 1) {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), true);
                } else {
                    ((UseCaseYesNo) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), false);
                }
                callbackCount.decrementAndGet();
            }

            @Override
            public void requestUserPlayerSelection(List<ActivePlayer> activePlayers) {
                if (mainGameFlowManager.getActiveGameUseCase() instanceof SithUseCase) {
                    //The sith kill boba Fett
                    if (mainGame.getCurrentRound() == 1) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), bobaFettPlayer.getPlayerId());
                        callbackCount.decrementAndGet();
                    } else if (mainGame.getCurrentRound() == 2) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), bobaFettPlayer.getPlayerId());
                    } else if (mainGame.getCurrentRound() == 3) {
                        ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), hanSoloPlayer.getPlayerId());
                    }
                } else if (mainGameFlowManager.getActiveGameUseCase() instanceof BobaFettUseCase) {
                    ((UseCaseId) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), jediPlayer.getPlayerId());
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void requestUserCardSelection(List<SithCard> availableSithCards) {
                //Han Solo is going to take a Sith card
                Assert.assertEquals(mainGame.getCurrentRound(), 1);
                ((UseCaseCard) mainGameFlowManager.getActiveGameUseCase()).onExecuteStep(mainGame.getCurrentStep(), MainGameDefaults.getSithCardDarthMaul());
                callbackCount.decrementAndGet();
            }

            @Override
            public void requestUserCardPeek(List<ActivePlayer> players, long delay) {
                //Maz kanata is going to look into a random card)
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(1);
                if (mainGame.getCurrentRound() == 1) {
                    callbackCount.decrementAndGet();
                }
            }

            @Override
            public void speak(int soundResId, int stringResId) {
                mainGameFlowManager.getActiveGameUseCase().onExecuteStep(mainGame.getCurrentStep());
            }

            @Override
            public void roundStatusChanged(boolean started, boolean gameOver) {
                //After the first round, only boba Fett and kylo renn are still alive on the good side, so the game is not yet over
                if (mainGame.getCurrentRound() == 1 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 2 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                } else if (mainGame.getCurrentRound() == 3 && !started) {
                    Assert.assertFalse(gameOver);
                    callbackCount.decrementAndGet();
                }
            }
        });

        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertEquals(hanSoloPlayer.getSithCard().getId(), MainGameDefaults.getSithCardDarthMaul().getId());
        Assert.assertEquals(mainGame.getLovers().first.getPlayerId(), lover1.getPlayerId());
        Assert.assertEquals(mainGame.getLovers().second.getPlayerId(), lover2.getPlayerId());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertFalse(mainGame.isGameOver());

        //Nobody is going to day during the day

        callbackCount.set(2);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        Assert.assertFalse(mainGame.isGameOver());

        List<Player> killedPlayers = new ArrayList<>();
        killedPlayers.add(Player.newBuilder()._id(sithPlayer.getPlayerId()).build());
        mainGame.killPlayers(killedPlayers);

        callbackCount.set(1);
        mainGameFlowManager.startNewRound();

        while (callbackCount.get() > 0) {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertFalse(mainGameFlowManager.isStarted());
        Assert.assertEquals(callbackCount.get(), 0);
        //Check if kylo renn converted to sith side
        Assert.assertEquals(kyloRenPlayer.getSide(), GameSide.SITH);
        Assert.assertFalse(mainGame.isGameOver());

        //Now Kill kylo ren and let the jedi win
        List<Player> killedPlayers2 = new ArrayList<>();
        killedPlayers2.add(Player.newBuilder()._id(kyloRenPlayer.getPlayerId()).build());
        mainGame.killPlayers(killedPlayers2);

        //Now the game should be over
        Assert.assertEquals(mainGame.getWinningTeam(), GameTeam.JEDI);
        List<ActivePlayer> alivePlayers = mainGame.getAlivePlayers();
        Assert.assertEquals(alivePlayers.size(), 2);
        Assert.assertTrue(alivePlayers.contains(lover1));
        Assert.assertTrue(alivePlayers.contains(lover2));
    }

    private class MainGameFlowCallBackAdapter implements MainGameFlowCallBack {

        @Override
        public void roundStatusChanged(final boolean started, final boolean gameOver) {
        }

        @Override
        public void requestUserPairPlayerSelection(final List<ActivePlayer> players) {
        }

        @Override
        public void showDelay(final long delay) {
        }

        @Override
        public void requestYesNoAnswer(final boolean disableYes, final int titleResId) {
        }

        @Override
        public void showPlayerYesNo(final ActivePlayer activePlayer, final boolean disableYes, final int titleResId) {
        }

        @Override
        public void requestUserPlayerSelection(final List<ActivePlayer> activePlayers) {
        }

        @Override
        public void requestUserCardSelection(final List<SithCard> availableSithCards) {
        }

        @Override
        public void requestUserCardPeek(final List<ActivePlayer> players, final long delay) {
        }

        @Override
        public void speak(final int soundResId, final int stringResId) {
        }

        @Override
        public void stackAndSpeak(final int soundResSId) {
        }

        @Override
        public void stackAndSpeak(final String soundResSId) {
        }

        @Override
        public void saveGame(final MainGame game) {
        }

        @Override
        public void deleteSavedGame() {
        }

        @Override
        public void playMusic(final int musicType) {
        }

        @Override
        public void stopPlayingMusic() {
        }

        @Override
        public void sendSMS(final int stringResId, final String number) {
        }
    }

}
