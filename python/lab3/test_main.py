import unittest
from main import gen_bin_tree


class TestGenBinTree(unittest.TestCase):
    def test_gen_my_variant(self):
        bin_tree = gen_bin_tree()
        self.assertEqual(bin_tree, {13: [{14: [{15: [{16: []},
                                                     {14: []}]},
                                               {13: [{14: []},
                                                     {12: []}]}]},
                                         {12: [{13: [{14: []},
                                                     {12: []}]},
                                               {11: [{12: []},
                                                     {10: []}]}]}]})

    def test_gen_with_other_func(self):
        bin_tree = gen_bin_tree(3, 5, lambda x: x + 1, lambda x: x ** 2)
        self.assertEqual(bin_tree, {5: [{6: [{7: []},
                                             {36: []}]},
                                        {25: [{26: []},
                                              {625: []}]}]})


if __name__ == '__main__':
    unittest.main()
