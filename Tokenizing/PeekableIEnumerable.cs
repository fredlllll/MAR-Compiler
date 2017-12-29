using System.Collections;
using System.Collections.Generic;

namespace MAR_Compiler.Tokenizing
{
    public class PeekableIEnumerable<T> :IEnumerable<T>
    {
        public IEnumerable<T> Enumerable { get; }
        IEnumerator<T> enumerator;

        List<T> peekedOnes = new List<T>();

        public PeekableIEnumerable(IEnumerable<T> enumerable)
        {
            Enumerable = enumerable;
            enumerator = enumerable.GetEnumerator();
        }

        public T Peek( int offset = 0)
        {
            if(peekedOnes.Count > offset)
            {
                return peekedOnes[offset];
            }
            for(int i =peekedOnes.Count; i<= offset; i++)
            {
                enumerator.MoveNext();
                peekedOnes.Add(enumerator.Current);
            }
            return enumerator.Current;
        }

        public IEnumerator<T> GetEnumerator()
        {
            while(true)
            {
                T retval;
                if(peekedOnes.Count > 0)
                {
                    retval = peekedOnes[0];
                    peekedOnes.RemoveAt(0);
                }
                else
                {
                    if(!enumerator.MoveNext())
                    {
                        break;
                    }
                    retval = enumerator.Current;
                }
                yield return retval;
            }
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }
    }
}
