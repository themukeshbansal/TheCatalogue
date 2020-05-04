# Resources

1. [Homepage](https://git-lfs.github.com/)

## Commands

1. git lfs ls-files : List all the files indexed for your user.

## Troubleshooting

1. this exceeds GitHub's file size limit of 100.00 MB
Even after indexing the large file. if you encounter this error
git filter-branch --tree-filter 'rm -rf <path_to_file>' HEAD

2. You need to run this command from the toplevel of the working tree.
Move to the head of your branch.
