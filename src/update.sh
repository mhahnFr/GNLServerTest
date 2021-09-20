#/bin/sh
version=v0.2
git fetch --all
newVersion=$(git tag | tail -n 1)
if [ "$version" != "$newVersion" ]; then
	echo "Update available. Will perform it now..."
	hash=$(git log -t $version --pretty='%H' -n 1)
	git reset $hash --hard
	git pull --force
fi
echo "Up to date"
