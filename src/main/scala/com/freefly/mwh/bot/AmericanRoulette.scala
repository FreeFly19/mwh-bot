package com.freefly.mwh.bot

trait RouletteNumberSupplier {
  protected def next(): Int

  final def getNext(): Int = {
    val r = next()
    if (r < -1 || r > 36) throw new IllegalStateException("Generated number cannot be less than -1(\"00\") or more than 36")
    r
  }
}

trait BidCell {
  val rate: Int
  def isWin(n: Int): Boolean
  def winAmount(bidAmount: Int): Int = bidAmount * rate
}

class AmericanRoulette(numberSupplier: RouletteNumberSupplier) extends Roulette[Seq[RouletteBid], RollResult] {
  def roll(bids: Seq[RouletteBid]): RollResult = {
    val i = numberSupplier.getNext()

    val (winBids, lostBids) = bids.partition(_.bidCell.isWin(i))

    RollResult(i, winBids, lostBids)
  }
}

case class RollResult(number: Int, winBids: Seq[RouletteBid], looseBids: Seq[RouletteBid]) {
  def winAmount: Int = winBids.map(_.winAmount).sum
  def bidsSum: Int = (winBids ++ looseBids).map(_.amount).sum
}

case class RouletteBid(amount: Int, bidCell: BidCell) {
  if (amount < 1) throw new IllegalArgumentException("Bid cannot be equals or less than 0")
  def winAmount: Int = bidCell.winAmount(amount)
}


trait StraightCell extends BidCell {
  val n: Int
  override val rate = 36
  override def isWin(n: Int): Boolean = n == this.n
}

case class Number(private val number: Int) extends StraightCell {
  if (number < -1 || number > 36) throw new IllegalStateException("Number cannot be less than -1(\"00\") or more than 36")
  override val n: Int = number
}

case object DoubleZero extends StraightCell {
  override val n: Int = -1
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