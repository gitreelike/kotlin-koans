package iii_conventions

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate): Int {
        if (year != other.year) {
            return year - other.year
        } else if (month != other.month) {
            return month - other.month
        } else {
            return dayOfMonth - other.dayOfMonth
        }
    }

    private fun addDays(days: Int): MyDate {
        val overflownDay = dayOfMonth + days

        if (overflownDay >= 31) {
            return MyDate(year, month, overflownDay - 31).addMonths(1)
        } else {
            return MyDate(year, month, overflownDay)
        }
    }

    private fun addMonths(months: Int): MyDate {
        val overflownMonth = month + months

        if (overflownMonth >= 12) {
            return MyDate(year + 1, overflownMonth - 12, dayOfMonth)
        } else {
            return MyDate(year, overflownMonth, dayOfMonth)
        }
    }

    operator fun plus(rhs: TimeInterval): MyDate {
        when (rhs) {
            TimeInterval.DAY -> return this.addDays(1)
            TimeInterval.WEEK -> return this.addDays(7)
            TimeInterval.YEAR -> return MyDate(year + 1, month, dayOfMonth)
        }
    }

    operator fun plus(rhs: BetterTimeInterval): MyDate {
        when (rhs.interval) {
            TimeInterval.DAY -> return this.addDays(rhs.multiplicity)
            TimeInterval.WEEK -> return this.addDays(7 * rhs.multiplicity)
            TimeInterval.YEAR -> return MyDate(year + rhs.multiplicity, month, dayOfMonth)
        }
    }
}

operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

enum class TimeInterval {
    DAY,
    WEEK,
    YEAR
}

operator fun TimeInterval.times(rhs: Int): BetterTimeInterval {
    return BetterTimeInterval(this, rhs)
}

class BetterTimeInterval(val interval: TimeInterval, val multiplicity: Int = 1) {
}

class DateRangeIterator(val dateRange: DateRange) : Iterator<MyDate> {
    var currentDate: MyDate = dateRange.start

    override fun hasNext(): Boolean {
        return currentDate <= dateRange.endInclusive
    }

    override fun next(): MyDate {
        val result = currentDate
        currentDate = currentDate.nextDay()
        return result
    }
}

class DateRange(override val start: MyDate, override val endInclusive: MyDate) : ClosedRange<MyDate>, Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate> = DateRangeIterator(this)

    override fun contains(value: MyDate): Boolean {
        return start <= value && value <= endInclusive
    }
}
