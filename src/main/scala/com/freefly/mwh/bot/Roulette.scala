package com.freefly.mwh.bot

trait Roulette[Bids, Result] {
  def roll(bids: Bids): Result
}