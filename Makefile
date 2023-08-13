setup:
	make -C app setup

clean:
	make -C app clean

build:
	make -C app build
	
start:
	make -C app start	

install:
	make -C app install

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint

.PHONY: build
