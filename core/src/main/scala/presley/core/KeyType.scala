package main.scala.presley.core

import presleydb.core.SingleKeyType
import scodec.bits.ByteVector

trait KeyType {
    type T

    def ordering: Ordering[T]

    def toBytes(key: T): ByteVector

    def fromBytes(bytes: ByteVector): T

    def fromString(str: String): T

    def getKeyFunc(columnNumbers: Array[Int]): RowReader => T

    def isSegmentType: Boolean = false

    def size(key: T): Int

}


import SingleKeyTypes._


case class NullKeyValue(colIndex: Int) extends Exception("Null value for col index $colIndex")


abstract class SingleKeyTypeBase[K : Ordering : TypedFieldExtractor] extends KeyType {
    type T = K

    override def getKeyFunc(columnNumbers: Array[Int]): (Any) => T = {
        require(columnNumbers.size == 1)
        val columnNum = columnNumbers(0)

       (r : RowReader) => {
          if (r.notNull(columnNum)) {
            extractor.getField(r, columnNum)
          } else {
            throw NullKeyValue(columnNum)
          }
       }
    }


    def ordering: Ordering[T] = implicitly[Ordering[K]]

    def extractor: TypedFieldExtractor[T] = implicitly[TypedFieldExtractor[K]]
}


case class CompositeOrdering(atomTypes: Seq[SingleKeyType]) extends Ordering[Seq[_]] {
    override def compare(x: Seq[_], y: Seq[_]): Int = {
        if(x.length == y.length && x.length == atomTypes.length) {
            for(i<-0 until x.length optimized) {
                val keyType = atomTypes(i)
                val res = keyType.ordering.compare(x(i).asInstanceOf[keyType.T], y(i).asInstanceOf[keyType.T])

                if(res != 0) res
            }

            return 0
        }

        throw new IllegalArgumentException("Comparing wrong composite types")
    }
}



case class CompositeKeyType(atomTypes: Seq[SingleKeyType]) extends KeyType {

}