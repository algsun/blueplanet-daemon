.PHONY: all clean

#ARCH=arm-linux-
ARCH=
CC=$(ARCH)gcc

MKDIR = mkdir
RM = rm
RMFLAGS = -fr

DIR_OBJS = ./objs
DIR_EXES = ./exes
DIR_DEPS = ./deps
DIRS     = $(DIR_OBJS) $(DIR_EXES) $(DIR_DEPS)

TAR  = searchtime
#TAR := $(addprefix $(DIR_EXES)/, $(TAR))      #put iGWmaind to ./exes directory

SRCS  = $(wildcard *.c)
OBJS  = $(SRCS:.c=.o)
OBJS := $(addprefix $(DIR_OBJS)/, $(OBJS))    #put all objs to ./objs directory

DEPS  = $(SRCS:.c=.dep)
DEPS := $(addprefix $(DIR_DEPS)/, $(DEPS))    #put all deps to ./deps directory

ifeq ("$(wildcard $(DIR_OBJS))", "")
DEP_DIR_OBJS := $(DIR_OBJS)
endif
ifeq ("$(wildcard $(DIR_EXES))", "")
DEP_DIR_EXES := $(DIR_EXES)
endif
ifeq ("$(wildcard $(DIR_DEPS))", "")
DEP_DIR_DEPS := $(DIR_DEPS)
endif

all: $(TAR)

ifneq ($(MAKECMDGOALS), clean)
include $(DEPS)
endif

$(DIRS):
	$(MKDIR) $@
$(TAR): $(DEP_DIR_EXES) $(OBJS)
	$(CC) -o $@ $(filter %.o, $^) -lpthread
$(DIR_OBJS)/%.o: $(DEP_DIR_OBJS) %.c
	$(CC) -o $@ -c $(filter %.c, $^)
$(DIR_DEPS)/%.dep: $(DEP_DIR_DEPS) %.c
	@echo "Creating $@ ..."
	@set -e; \
	$(RM) $(RMFLAGS) $@.tmp ; \
	$(CC) -E -MM $(filter %.c, $^) > $@.tmp ; \
	sed 's,\(.*\)\.o[ :]*,objs/\1.o $@: ,g' < $@.tmp > $@ ; \
	$(RM) $(RMFLAGS) $@.tmp
clean:
	$(RM) $(RMFLAGS) $(DIRS)
