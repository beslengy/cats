package com.molchanov.cats.ui.catcard

enum class VoteStates (
    val voteValue: Int
        ){
    VOTE_UP(1),
    VOTE_DOWN(0),
    NOT_VOTED(-1);
}