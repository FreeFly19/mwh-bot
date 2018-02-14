package com.freefly.mwh.bot

case class RollResult(number: Int, win: Map[BidCell, Int])
case class RouletteBid(amount: Int, bidCell: BidCell)

trait BidCell {
  val rate: Int
  def isWin(n: Int): Boolean
}

case class Number(n: Int) extends BidCell {
  override val rate = 36
  override def isWin(n: Int): Boolean = n == this.n
}
case object Even extends BidCell {
  override val rate = 2
  override def isWin(n: Int): Boolean = n > 0 && n % 2 == 0
}
case object Odd extends BidCell {
  override val rate = 2
  override def isWin(n: Int): Boolean = n > 0 && n % 2 == 1
}
case object Red extends BidCell {
  val redCells = Set(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36)

  override val rate = 2
  override def isWin(n: Int): Boolean = n > 0 && redCells.contains(n)
}
case object Black extends BidCell {
  override val rate = 2
  override def isWin(n: Int): Boolean = n > 0 && !Red.redCells.contains(n)
}
case object OneToEighteen extends BidCell {
  override val rate = 2
  override def isWin(n: Int): Boolean = n > 0 && n < 19
}
case object NineteenToThirtySix extends BidCell {
  override val rate = 2
  override def isWin(n: Int): Boolean = n > 18
}

class AmericanRoulette(numberSupplier: RouletteNumberSupplier) extends Roulette[Seq[RouletteBid], RollResult] {
  def roll(bids: Seq[RouletteBid]): RollResult = {
    val i = numberSupplier.next()
    if (i < -1 || i > 36) throw new IllegalStateException("Generated number cannot be less than -1(\"00\") or more than 36")
    RollResult(i, Map())
  }
}

trait RouletteNumberSupplier {
  def next(): Int
}
