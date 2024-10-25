package io.kresult.problem

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProblemTest7xx {

  @Test
  fun `Meh can be built and printed to JSON`() {
    val problem = Problem.Meh()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/meh","status": 701,"title": "Meh"}"""
  }

  @Test
  fun `Explosion can be built and printed to JSON`() {
    val problem = Problem.Explosion()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/explosion","status": 703,"title": "Explosion"}"""
  }

  @Test
  fun `DeleteYourAccount can be built and printed to JSON`() {
    val problem = Problem.DeleteYourAccount()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/delete-your-account","status": 706,"title": "Delete Your Account"}"""
  }

  @Test
  fun `Unpossible can be built and printed to JSON`() {
    val problem = Problem.Unpossible()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/unpossible","status": 720,"title": "Unpossible"}"""
  }

  @Test
  fun `KnownUnknowns can be built and printed to JSON`() {
    val problem = Problem.KnownUnknowns()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/known-unknowns","status": 721,"title": "Known Unknowns"}"""
  }

  @Test
  fun `UnknownUnknowns can be built and printed to JSON`() {
    val problem = Problem.UnknownUnknowns()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/unknown-unknowns","status": 722,"title": "Unknown Unknowns"}"""
  }

  @Test
  fun `Tricky can be built and printed to JSON`() {
    val problem = Problem.Tricky()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/tricky","status": 723,"title": "Tricky"}"""
  }

  @Test
  fun `ThisLineShouldBeUnreachable can be built and printed to JSON`() {
    val problem = Problem.ThisLineShouldBeUnreachable()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/this-line-should-be-unreachable","status": 724,"title": "This line should be unreachable"}"""
  }

  @Test
  fun `ItWorksOnMyMachine can be built and printed to JSON`() {
    val problem = Problem.ItWorksOnMyMachine()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/it-works-on-my-machine","status": 725,"title": "It works on my machine"}"""
  }

  @Test
  fun `ItsAFeatureNotABug can be built and printed to JSON`() {
    val problem = Problem.ItsAFeatureNotABug()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/its-a-feature-not-a-bug","status": 726,"title": "It's a feature, not a bug"}"""
  }

  @Test
  fun `ThirtyTwoBitsIsPlenty can be built and printed to JSON`() {
    val problem = Problem.ThirtyTwoBitsIsPlenty()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/32-bits-is-plenty","status": 727,"title": "32 bits is plenty"}"""
  }

  @Test
  fun `ItWorksInMyTimezone can be built and printed to JSON`() {
    val problem = Problem.ItWorksInMyTimezone()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/it-works-in-my-timezone","status": 728,"title": "It works in my timezone"}"""
  }
}
